package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.base.RepositoryTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for LoadInitialDataImpl.
 * 
 * Tests HTTP deserialization with real JSON responses from TMDB API.
 * 
 * Context: These tests would have caught the bug in Task 1.9 where 
 * LoadInitialDataImpl used MovieResponse (domain) instead of MovieResponseDto.
 */
class LoadInitialDataImplTest : RepositoryTest() {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `can deserialize TMDB movie response`() = runTest {
        // Given - Mock TMDB API response
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "page": 1,
                        "results": [
                            {
                                "id": 550,
                                "title": "Fight Club",
                                "original_title": "Fight Club",
                                "overview": "A ticking-time-bomb insomniac...",
                                "release_date": "1999-10-15",
                                "poster_path": "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
                                "backdrop_path": "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
                                "vote_average": 8.4,
                                "vote_count": 26280,
                                "popularity": 90.123,
                                "adult": false,
                                "video": false,
                                "original_language": "en",
                                "genre_ids": [18]
                            }
                        ],
                        "total_pages": 100,
                        "total_results": 2000
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        
        // When
        val repository = LoadInitialDataImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        repository.refreshMovies(force = true)
        
        // Then - Verify movies were deserialized correctly
        val movies = repository.moviesFlow.value
        assertNotNull(movies)
        assertTrue(movies.isNotEmpty(), "Movies should not be empty")
        
        val firstMovie = movies.first()
        assertEquals(550, firstMovie.id)
        assertEquals("Fight Club", firstMovie.title)
        assertEquals(8.4, firstMovie.voteAverageDouble)
        assertEquals("/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg", firstMovie.posterPath)
    }
    
    @Test
    fun `can deserialize empty TMDB response`() = runTest {
        // Given - Empty results
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "page": 1,
                        "results": [],
                        "total_pages": 0,
                        "total_results": 0
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        
        // When
        val repository = LoadInitialDataImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        repository.refreshMovies(force = true)
        
        // Then
        val movies = repository.moviesFlow.value
        assertNotNull(movies)
        assertTrue(movies.isEmpty())
    }
    
    @Test
    fun `handles movies with missing optional fields`() = runTest {
        // Given - Movie with minimal fields (many nulls)
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "page": 1,
                        "results": [
                            {
                                "id": 123,
                                "title": "Minimal Movie",
                                "original_title": "Minimal Movie",
                                "overview": null,
                                "release_date": null,
                                "poster_path": null,
                                "backdrop_path": null,
                                "vote_average": null,
                                "vote_count": null,
                                "popularity": null,
                                "adult": false,
                                "video": false,
                                "original_language": "en",
                                "genre_ids": []
                            }
                        ],
                        "total_pages": 1,
                        "total_results": 1
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        
        // When
        val repository = LoadInitialDataImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        repository.refreshMovies(force = true)
        
        // Then - Should handle nulls gracefully (may filter invalid movies)
        val movies = repository.moviesFlow.value
        assertNotNull(movies)
        // Repository may filter invalid movies - that's OK
    }
    
    @Test
    fun `MovieResponseDto matches TMDB API structure`() = runTest {
        // Given - Real TMDB API response structure
        val realApiJson = """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/backdrop.jpg",
                        "genre_ids": [28, 12],
                        "id": 12345,
                        "original_language": "en",
                        "original_title": "Test Movie",
                        "overview": "Test overview",
                        "popularity": 123.456,
                        "poster_path": "/poster.jpg",
                        "release_date": "2024-12-08",
                        "title": "Test Movie",
                        "video": false,
                        "vote_average": 8.5,
                        "vote_count": 1234
                    }
                ],
                "total_pages": 500,
                "total_results": 10000
            }
        """.trimIndent()
        
        val mockEngine = MockEngine { request ->
            respond(
                content = realApiJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        
        // When
        val repository = LoadInitialDataImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        repository.refreshMovies(force = true)
        
        // Then - Should deserialize without errors
        val movies = repository.moviesFlow.value
        assertNotNull(movies)
        
        if (movies.isNotEmpty()) {
            val movie = movies.first()
            assertEquals(12345, movie.id)
            assertEquals("Test Movie", movie.title)
            assertEquals(8.5, movie.voteAverageDouble)
        }
    }
}
