package org.lanzadera.proyectos.data.repository

import app.cash.turbine.test
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
import kotlin.test.assertTrue

/**
 * Integration tests for BooksRepositoryImpl.
 * 
 * Tests HTTP deserialization with real JSON responses from Google Books API.
 * 
 * Context: These tests verify that:
 * 1. Google Books API responses are correctly deserialized using GoogleBooksResponseDto
 * 2. VolumeItemDto → Book mapping works correctly
 * 3. Error handling works (network failures, empty responses)
 * 4. Cache behavior works (force refresh, query changes)
 */
class BooksRepositoryImplTest : RepositoryTest() {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Test
    fun `can deserialize Google Books API response`() = runTest {
        // Given: Mock Google Books API response
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "kind": "books#volumes",
                        "totalItems": 1,
                        "items": [
                            {
                                "kind": "books#volume",
                                "id": "abc123",
                                "volumeInfo": {
                                    "title": "Test Book",
                                    "authors": ["Author One", "Author Two"],
                                    "description": "A test book description",
                                    "publishedDate": "2024-01-15",
                                    "imageLinks": {
                                        "thumbnail": "https://books.google.com/books/content?id=abc123&printsec=frontcover&img=1&zoom=1"
                                    }
                                }
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

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json,
            apiKey = "",
            maxResultsPerQuery = 40
        )

        // When: Refresh books
        repository.refreshBooks(force = true, query = "subject:fiction")

        // Then: Books should be deserialized correctly
        repository.booksFlow.test {
            val books = awaitItem()
            assertTrue(books.isNotEmpty(), "Books should be loaded")
            assertEquals("abc123", books.first().id)
            assertEquals("Test Book", books.first().title)
            assertEquals(2, books.first().authors?.size)
            assertEquals("Author One", books.first().authors?.get(0))
        }
    }

    @Test
    fun `refreshFictionBooks fetches fiction category`() = runTest {
        // Given: Mock response for fiction query
        val mockEngine = MockEngine { request ->
            // Verify query contains "subject:fiction"
            assertTrue(request.url.toString().contains("subject:fiction"))
            
            respond(
                content = """
                    {
                        "items": [
                            {
                                "id": "fiction1",
                                "volumeInfo": {
                                    "title": "Fiction Book",
                                    "authors": ["Fiction Author"]
                                }
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

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh fiction books
        repository.refreshFictionBooks(force = true)

        // Then: Fiction books flow should be updated
        repository.fictionBooksFlow.test {
            val books = awaitItem()
            assertEquals(1, books.size)
            assertEquals("Fiction Book", books.first().title)
        }
    }

    @Test
    fun `refreshScienceBooks fetches science category`() = runTest {
        // Given: Mock response for science query
        val mockEngine = MockEngine { request ->
            assertTrue(request.url.toString().contains("subject:science"))
            
            respond(
                content = """
                    {
                        "items": [
                            {
                                "id": "science1",
                                "volumeInfo": {
                                    "title": "Science Book",
                                    "description": "A book about science"
                                }
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

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh science books
        repository.refreshScienceBooks(force = true)

        // Then: Science books flow should be updated
        repository.scienceBooksFlow.test {
            val books = awaitItem()
            assertEquals(1, books.size)
            assertEquals("Science Book", books.first().title)
        }
    }

    @Test
    fun `handles missing imageLinks gracefully`() = runTest {
        // Given: Response with no imageLinks
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "items": [
                            {
                                "id": "noimage1",
                                "volumeInfo": {
                                    "title": "Book Without Image",
                                    "authors": ["Author"]
                                }
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

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh books
        repository.refreshBooks(force = true, query = "test")

        // Then: Should handle missing thumbnail gracefully
        repository.booksFlow.test {
            val books = awaitItem()
            assertEquals(1, books.size)
            assertEquals("Book Without Image", books.first().title)
            assertEquals(null, books.first().thumbnail)
        }
    }

    @Test
    fun `normalizes thumbnail URLs correctly`() = runTest {
        // Given: Response with various thumbnail URL formats
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "items": [
                            {
                                "id": "book1",
                                "volumeInfo": {
                                    "title": "Book 1",
                                    "imageLinks": {
                                        "thumbnail": "//books.google.com/image.jpg"
                                    }
                                }
                            },
                            {
                                "id": "book2",
                                "volumeInfo": {
                                    "title": "Book 2",
                                    "imageLinks": {
                                        "thumbnail": "http://books.google.com/image.jpg"
                                    }
                                }
                            },
                            {
                                "id": "book3",
                                "volumeInfo": {
                                    "title": "Book 3",
                                    "imageLinks": {
                                        "thumbnail": "https://books.google.com/image.jpg"
                                    }
                                }
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

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh books
        repository.refreshBooks(force = true, query = "test")

        // Then: All thumbnails should be normalized to https://
        repository.booksFlow.test {
            val books = awaitItem()
            assertEquals(3, books.size)
            
            // // → https://
            assertTrue(books[0].thumbnail!!.startsWith("https://"))
            
            // http:// → https://
            assertTrue(books[1].thumbnail!!.startsWith("https://"))
            
            // https:// → unchanged
            assertTrue(books[2].thumbnail!!.startsWith("https://"))
        }
    }

    @Test
    fun `handles network errors gracefully`() = runTest {
        // Given: Mock engine that returns error
        val mockEngine = MockEngine { request ->
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh books (should handle error)
        repository.refreshBooks(force = true, query = "test")

        // Then: Flow should return empty list (graceful degradation)
        repository.booksFlow.test {
            val books = awaitItem()
            assertTrue(books.isEmpty())
        }
    }

    @Test
    fun `respects cache when force is false`() = runTest {
        // Given: Mock engine that counts requests
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = """{"items": [{"id": "test", "volumeInfo": {"title": "Test"}}]}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh twice with force=false
        repository.refreshBooks(force = true, query = "test")
        repository.refreshBooks(force = false, query = "test")

        // Then: Should only make 1 request (second is cached)
        assertEquals(1, requestCount)
    }

    @Test
    fun `refreshes when query changes even if force is false`() = runTest {
        // Given: Mock engine that counts requests
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = """{"items": [{"id": "test", "volumeInfo": {"title": "Test"}}]}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh with different queries
        repository.refreshBooks(force = true, query = "fiction")
        repository.refreshBooks(force = false, query = "science")

        // Then: Should make 2 requests (query changed)
        assertEquals(2, requestCount)
    }

    @Test
    fun `handles empty API response`() = runTest {
        // Given: Empty response from API
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"items": null}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh books
        repository.refreshBooks(force = true, query = "nonexistent")

        // Then: Should handle gracefully with empty list
        repository.booksFlow.test {
            val books = awaitItem()
            assertTrue(books.isEmpty())
        }
    }

    @Test
    fun `uses default query when query is blank`() = runTest {
        // Given: Mock engine that captures the request URL
        var capturedUrl = ""
        val mockEngine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond(
                content = """{"items": []}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repository = BooksRepositoryImpl(
            client = httpClient,
            json = json
        )

        // When: Refresh with blank query
        repository.refreshBooks(force = true, query = "")

        // Then: Should use default query "subject:fiction&printType=books"
        assertTrue(capturedUrl.contains("subject:fiction"))
        assertTrue(capturedUrl.contains("printType=books"))
    }
}
