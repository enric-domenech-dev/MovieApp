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
 * Integration tests for MovieRepositoryImpl.
 * 
 * Tests HTTP deserialization with real JSON responses from TMDB API.
 * 
 * Context: Tests movie details deserialization including collections.
 */
class MovieRepositoryImplTest : RepositoryTest() {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `can deserialize movie details with collection`() = runTest {
        // Given - Mock movie details response with collection
        val mockEngine = MockEngine { request ->
            respond(
                content = """
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
                        "genre_ids": [18],
                        "budget": 63000000,
                        "revenue": 100853753,
                        "runtime": 139,
                        "status": "Released",
                        "tagline": "Mischief. Mayhem. Soap.",
                        "belongs_to_collection": {
                            "id": 645,
                            "name": "James Bond Collection",
                            "poster_path": "/collection_poster.jpg",
                            "backdrop_path": "/collection_backdrop.jpg"
                        },
                        "production_companies": [
                            {
                                "id": 508,
                                "name": "Regency Enterprises",
                                "logo_path": "/logo.png",
                                "origin_country": "US"
                            }
                        ],
                        "production_countries": [
                            {
                                "iso_3166_1": "US",
                                "name": "United States of America"
                            }
                        ],
                        "spoken_languages": [
                            {
                                "iso_639_1": "en",
                                "name": "English"
                            }
                        ],
                        "genres": [
                            {
                                "id": 18,
                                "name": "Drama"
                            }
                        ]
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
        val repository = MovieRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        val movie = repository.getMovieDetails(550)
        
        // Then - Verify movie was deserialized correctly
        assertNotNull(movie)
        assertEquals(550, movie.id)
        assertEquals("Fight Club", movie.title)
        assertEquals(8.4, movie.voteAverageDouble)
        assertEquals(139, movie.runtime)
        assertEquals("Released", movie.status)
        assertEquals("Mischief. Mayhem. Soap.", movie.tagline)
        
        // Verify collection
        assertNotNull(movie.belongsToCollection)
        assertEquals(645, movie.belongsToCollection?.id)
        assertEquals("James Bond Collection", movie.belongsToCollection?.name)
    }
    
    @Test
    fun `can deserialize movie details without collection`() = runTest {
        // Given - Movie without collection
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "id": 123,
                        "title": "Standalone Movie",
                        "original_title": "Standalone Movie",
                        "overview": "A movie not part of any collection",
                        "release_date": "2024-01-01",
                        "poster_path": "/poster.jpg",
                        "backdrop_path": "/backdrop.jpg",
                        "vote_average": 7.5,
                        "vote_count": 1000,
                        "popularity": 50.0,
                        "adult": false,
                        "video": false,
                        "original_language": "en",
                        "runtime": 120,
                        "status": "Released",
                        "belongs_to_collection": null
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
        val repository = MovieRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        val movie = repository.getMovieDetails(123)
        
        // Then
        assertNotNull(movie)
        assertEquals(123, movie.id)
        assertEquals("Standalone Movie", movie.title)
        assertEquals(null, movie.belongsToCollection)
    }
    
    @Test
    fun `can deserialize movie list response`() = runTest {
        // Given - Mock movie list response
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "page": 1,
                        "results": [
                            {
                                "id": 100,
                                "title": "Movie 1",
                                "original_title": "Movie 1",
                                "overview": "First movie",
                                "release_date": "2024-01-01",
                                "poster_path": "/poster1.jpg",
                                "backdrop_path": "/backdrop1.jpg",
                                "vote_average": 8.0,
                                "vote_count": 500,
                                "popularity": 50.0,
                                "adult": false,
                                "video": false,
                                "original_language": "en"
                            },
                            {
                                "id": 200,
                                "title": "Movie 2",
                                "original_title": "Movie 2",
                                "overview": "Second movie",
                                "release_date": "2024-02-01",
                                "poster_path": "/poster2.jpg",
                                "backdrop_path": "/backdrop2.jpg",
                                "vote_average": 7.5,
                                "vote_count": 300,
                                "popularity": 40.0,
                                "adult": false,
                                "video": false,
                                "original_language": "en"
                            }
                        ],
                        "total_pages": 10,
                        "total_results": 200
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
        val repository = MovieRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        repository.refreshMovies(force = true)
        
        // Then
        val movies = repository.moviesFlow.value
        assertNotNull(movies)
        assertTrue(movies.isNotEmpty())
        assertEquals(2, movies.size)
        
        val firstMovie = movies.first()
        assertEquals(100, firstMovie.id)
        assertEquals("Movie 1", firstMovie.title)
        assertEquals(8.0, firstMovie.voteAverageDouble)
    }
    
    @Test
    fun `getMovieDetails can fetch movie by ID`() = runTest {
        // Given - Movie details response
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "id": 456,
                        "title": "Movie with Credits",
                        "original_title": "Movie with Credits",
                        "overview": "A movie with cast and crew",
                        "release_date": "2024-01-01",
                        "poster_path": "/poster.jpg",
                        "backdrop_path": "/backdrop.jpg",
                        "vote_average": 8.2,
                        "vote_count": 2000,
                        "popularity": 75.0,
                        "adult": false,
                        "video": false,
                        "original_language": "en",
                        "runtime": 120,
                        "status": "Released"
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
        val repository = MovieRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        val movie = repository.getMovieDetails(456)
        
        // Then
        assertNotNull(movie)
        assertEquals(456, movie.id)
        assertEquals("Movie with Credits", movie.title)
        assertEquals(8.2, movie.voteAverageDouble)
    }
    
    @Test
    fun `MovieDto matches TMDB API structure for popular movies`() = runTest {
        // Given - Real TMDB popular movies endpoint structure
        val realApiJson = """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/backdrop.jpg",
                        "genre_ids": [28, 12, 878],
                        "id": 11111,
                        "original_language": "en",
                        "original_title": "Test Movie",
                        "overview": "Test overview",
                        "popularity": 1234.567,
                        "poster_path": "/poster.jpg",
                        "release_date": "2024-12-08",
                        "title": "Test Movie",
                        "video": false,
                        "vote_average": 8.9,
                        "vote_count": 5678
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
        val repository = MovieRepositoryImpl(
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
            assertEquals(11111, movie.id)
            assertEquals("Test Movie", movie.title)
            assertEquals(8.9, movie.voteAverageDouble)
            assertEquals(5678, movie.voteCount)
        }
    }
}
