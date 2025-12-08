package org.lanzadera.proyectos.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for SearchRepositoryImpl.
 * Tests TMDB search API deserialization and search functionality.
 */
class SearchRepositoryImplTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Test
    fun `searchMovies deserializes TMDB movie search response`() = runTest {
        // Given - Mock TMDB movie search response
        val mockResponse = """
        {
          "page": 1,
          "results": [
            {
              "adult": false,
              "backdrop_path": "/path.jpg",
              "genre_ids": [28, 12],
              "id": 550,
              "original_language": "en",
              "original_title": "Fight Club",
              "overview": "A ticking-time-bomb insomniac...",
              "popularity": 61.416,
              "poster_path": "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
              "release_date": "1999-10-15",
              "title": "Fight Club",
              "video": false,
              "vote_average": 8.433,
              "vote_count": 26280
            }
          ],
          "total_pages": 1,
          "total_results": 1
        }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchMovies("Fight Club", page = 1)

        // Then
        assertEquals(1, results.size)
        assertEquals(550, results[0].id)
        assertEquals("Fight Club", results[0].title)
        assertEquals(8.433, results[0].voteAverageDouble)
    }

    @Test
    fun `searchMovies handles multiple results`() = runTest {
        // Given - Multiple movie results
        val mockResponse = """
        {
          "page": 1,
          "results": [
            {"id": 1, "title": "Movie 1", "vote_average": 7.5},
            {"id": 2, "title": "Movie 2", "vote_average": 8.0},
            {"id": 3, "title": "Movie 3", "vote_average": 6.5}
          ],
          "total_pages": 1,
          "total_results": 3
        }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchMovies("Movie", page = 1)

        // Then
        assertEquals(3, results.size)
        assertEquals("Movie 1", results[0].title)
        assertEquals("Movie 2", results[1].title)
        assertEquals("Movie 3", results[2].title)
    }

    @Test
    fun `searchMovies returns empty list on network error`() = runTest {
        // Given - Mock network error
        val mockEngine = MockEngine { request ->
            respond(
                content = "Network Error",
                status = HttpStatusCode.InternalServerError
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchMovies("Test", page = 1)

        // Then - Graceful degradation
        assertTrue(results.isEmpty())
    }

    @Test
    fun `searchMovies returns empty list when no results found`() = runTest {
        // Given - Empty results
        val mockResponse = """
        {
          "page": 1,
          "results": [],
          "total_pages": 0,
          "total_results": 0
        }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchMovies("NonexistentMovie123456", page = 1)

        // Then
        assertTrue(results.isEmpty())
    }

    @Test
    fun `searchTvShows deserializes TMDB TV search response`() = runTest {
        // Given - Mock TMDB TV show search response
        val mockResponse = """
        {
          "page": 1,
          "results": [
            {
              "adult": false,
              "backdrop_path": "/path.jpg",
              "genre_ids": [18],
              "id": 1396,
              "origin_country": ["US"],
              "original_language": "en",
              "original_name": "Breaking Bad",
              "overview": "A high school chemistry teacher...",
              "popularity": 406.812,
              "poster_path": "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
              "first_air_date": "2008-01-20",
              "name": "Breaking Bad",
              "vote_average": 8.9,
              "vote_count": 12067
            }
          ],
          "total_pages": 1,
          "total_results": 1
        }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchTvShows("Breaking Bad", page = 1)

        // Then
        assertEquals(1, results.size)
        assertEquals(1396, results[0].id)
        assertEquals("Breaking Bad", results[0].name)
        assertEquals(8.9, results[0].voteAverageDouble)
    }

    @Test
    fun `searchTvShows handles multiple results`() = runTest {
        // Given - Multiple TV show results
        val mockResponse = """
        {
          "page": 1,
          "results": [
            {"id": 1, "name": "Show 1", "vote_average": 8.5, "first_air_date": "2020-01-01"},
            {"id": 2, "name": "Show 2", "vote_average": 7.0, "first_air_date": "2021-01-01"},
            {"id": 3, "name": "Show 3", "vote_average": 9.0, "first_air_date": "2019-01-01"}
          ],
          "total_pages": 1,
          "total_results": 3
        }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchTvShows("Show", page = 1)

        // Then
        assertEquals(3, results.size)
        assertEquals("Show 1", results[0].name)
        assertEquals("Show 2", results[1].name)
        assertEquals("Show 3", results[2].name)
    }

    @Test
    fun `searchTvShows returns empty list on network error`() = runTest {
        // Given - Mock network error
        val mockEngine = MockEngine { request ->
            respond(
                content = "Network Error",
                status = HttpStatusCode.InternalServerError
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchTvShows("Test", page = 1)

        // Then - Graceful degradation
        assertTrue(results.isEmpty())
    }

    @Test
    fun `searchTvShows handles missing fields gracefully`() = runTest {
        // Given - Response with missing optional fields
        val mockResponse = """
        {
          "page": 1,
          "results": [
            {
              "id": 100,
              "name": "Minimal Show",
              "vote_average": 7.0
            }
          ],
          "total_pages": 1,
          "total_results": 1
        }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchTvShows("Minimal", page = 1)

        // Then - Should still deserialize successfully
        assertEquals(1, results.size)
        assertNotNull(results[0])
        assertEquals(100, results[0].id)
        assertEquals("Minimal Show", results[0].name)
    }

    @Test
    fun `searchMovies preserves all critical fields`() = runTest {
        // Given - Full movie data
        val mockResponse = """
        {
          "page": 1,
          "results": [
            {
              "id": 999,
              "title": "Test Movie",
              "original_title": "Test Original",
              "overview": "Test overview",
              "poster_path": "/poster.jpg",
              "backdrop_path": "/backdrop.jpg",
              "release_date": "2023-01-15",
              "vote_average": 7.8,
              "vote_count": 1500,
              "popularity": 42.5,
              "genre_ids": [18, 28],
              "adult": false,
              "video": false,
              "original_language": "en"
            }
          ],
          "total_pages": 1,
          "total_results": 1
        }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@SearchRepositoryImplTest.json)
            }
        }

        val repository = SearchRepositoryImpl(client, json)

        // When
        val results = repository.searchMovies("Test", page = 1)

        // Then - Verify all fields are preserved
        assertEquals(1, results.size)
        val movie = results[0]
        assertEquals(999, movie.id)
        assertEquals("Test Movie", movie.title)
        assertEquals("Test Original", movie.originalTitle)
        assertEquals("Test overview", movie.overview)
        assertEquals("/poster.jpg", movie.posterPath)
        assertEquals("/backdrop.jpg", movie.backdropPath)
        assertEquals("2023-01-15", movie.releaseDate)
        assertEquals(7.8, movie.voteAverageDouble)
        assertEquals(1500, movie.voteCount)
        assertEquals(42.5, movie.popularity)
        assertEquals(false, movie.adult)
    }
}
