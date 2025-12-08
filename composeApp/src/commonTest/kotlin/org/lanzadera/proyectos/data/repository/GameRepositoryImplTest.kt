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
import org.lanzadera.proyectos.data.authentication.IGDBAuthManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Integration tests for GameRepositoryImpl.
 * 
 * Tests HTTP deserialization with real JSON responses from IGDB API.
 * 
 * Context: These tests verify that:
 * 1. IGDB API responses are correctly deserialized using GameDto
 * 2. Image URL construction works correctly (Cover.getImageUrl())
 * 3. Error handling works (network failures, authentication)
 * 4. Cache behavior works (TTL, force refresh)
 * 5. Game validation filters out invalid games
 */
class GameRepositoryImplTest : RepositoryTest() {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun createFakeAuthManager(): IGDBAuthManager {
        // Create a mock HttpClient that returns a fake token
        val authMockEngine = MockEngine { request ->
            respond(
                content = """{"access_token":"fake_token","expires_in":5184000,"token_type":"bearer"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val authClient = HttpClient(authMockEngine)
        
        return IGDBAuthManager(
            clientId = "fake_client_id",
            clientSecret = "fake_client_secret",
            httpClient = authClient,
            json = json
        )
    }

    @Test
    fun `can deserialize IGDB API response`() = runTest {
        // Given: Mock IGDB API response
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": 1942,
                            "name": "The Witcher 3: Wild Hunt",
                            "summary": "A story-driven, next-generation open world role-playing game.",
                            "rating": 94.5,
                            "rating_count": 1500,
                            "cover": {
                                "id": 123,
                                "image_id": "co1l4f"
                            },
                            "genres": [
                                {
                                    "id": 12,
                                    "name": "Role-playing (RPG)"
                                }
                            ],
                            "platforms": [
                                {
                                    "id": 6,
                                    "name": "PC (Microsoft Windows)"
                                },
                                {
                                    "id": 48,
                                    "name": "PlayStation 4"
                                }
                            ]
                        }
                    ]
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(this@GameRepositoryImplTest.json)
            }
        }

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh games
        repository.refreshGames(page = 1)

        // Then: Games should be deserialized correctly
        repository.gamesFlow.test {
            val games = awaitItem()
            assertTrue(games.isNotEmpty(), "Games should be loaded")
            assertEquals(1942, games.first().id)
            assertEquals("The Witcher 3: Wild Hunt", games.first().name)
            assertEquals(94.5, games.first().rating)
            assertEquals(2, games.first().platforms?.size)
            assertNotNull(games.first().cover)
        }
    }

    @Test
    fun `refreshPopularGames fetches popular category`() = runTest {
        // Given: Mock response for popular games query
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": 100,
                            "name": "Popular Game",
                            "cover": {
                                "image_id": "popular123"
                            },
                            "popularity": 150.5
                        }
                    ]
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

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh popular games
        repository.refreshPopularGames(page = 1)

        // Then: Popular games flow should be updated
        repository.popularGamesFlow.test {
            val games = awaitItem()
            assertEquals(1, games.size)
            assertEquals("Popular Game", games.first().name)
            assertEquals(150.5, games.first().popularity)
        }
    }

    @Test
    fun `refreshTopRatedGames fetches top rated category`() = runTest {
        // Given: Mock response for top rated games
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": 200,
                            "name": "Top Rated Game",
                            "rating": 98.7,
                            "cover": {
                                "image_id": "toprated456"
                            }
                        }
                    ]
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

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh top rated games
        repository.refreshTopRatedGames(page = 1)

        // Then: Top rated games flow should be updated
        repository.topRatedGamesFlow.test {
            val games = awaitItem()
            assertEquals(1, games.size)
            assertEquals("Top Rated Game", games.first().name)
            assertEquals(98.7, games.first().rating)
        }
    }

    @Test
    fun `refreshTrendingGames fetches trending category`() = runTest {
        // Given: Mock response for trending games
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": 300,
                            "name": "Trending Game",
                            "hype": 250,
                            "cover": {
                                "image_id": "trending789"
                            }
                        }
                    ]
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

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh trending games
        repository.refreshTrendingGames(page = 1)

        // Then: Trending games flow should be updated
        repository.trendingGamesFlow.test {
            val games = awaitItem()
            assertEquals(1, games.size)
            assertEquals("Trending Game", games.first().name)
            assertEquals(250, games.first().hype)
        }
    }

    @Test
    fun `cover image URL is constructed correctly`() = runTest {
        // Given: Game with cover image_id
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": 400,
                            "name": "Game With Cover",
                            "cover": {
                                "id": 12345,
                                "image_id": "co1l4f"
                            }
                        }
                    ]
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

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh games
        repository.refreshGames(page = 1)

        // Then: Cover URL should be constructed using IGDB format
        repository.gamesFlow.test {
            val games = awaitItem()
            val cover = games.first().cover
            assertNotNull(cover)
            
            val imageUrl = cover.getImageUrl()
            assertEquals("https://images.igdb.com/igdb/image/upload/t_cover_big/co1l4f.jpg", imageUrl)
        }
    }

    @Test
    fun `getGameDetails fetches specific game with screenshots`() = runTest {
        // Given: Mock response for game details
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": 500,
                            "name": "Detailed Game",
                            "summary": "Full game details",
                            "cover": {
                                "image_id": "detail123"
                            },
                            "screenshots": [
                                {
                                    "id": 1,
                                    "image_id": "screenshot1"
                                },
                                {
                                    "id": 2,
                                    "image_id": "screenshot2"
                                }
                            ]
                        }
                    ]
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

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Get game details
        val game = repository.getGameDetails(gameId = 500)

        // Then: Should return game with screenshots
        assertNotNull(game)
        assertEquals(500, game.id)
        assertEquals("Detailed Game", game.name)
        assertEquals(2, game.screenshots?.size)
    }

    @Test
    fun `getGameDetails returns null on error`() = runTest {
        // Given: Mock engine that returns error
        val mockEngine = MockEngine { request ->
            respond(
                content = "Unauthorized",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Get game details (should handle error)
        val game = repository.getGameDetails(gameId = 999)

        // Then: Should return null (graceful degradation)
        assertNull(game)
    }

    @Test
    fun `handles network errors gracefully`() = runTest {
        // Given: Mock engine that returns error
        val mockEngine = MockEngine { request ->
            respond(
                content = "Service Unavailable",
                status = HttpStatusCode.ServiceUnavailable,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh games (should handle error)
        repository.refreshGames(page = 1)

        // Then: Flow should remain empty (graceful degradation)
        repository.gamesFlow.test {
            val games = awaitItem()
            assertTrue(games.isEmpty())
        }
    }

    @Test
    fun `filters out invalid games without required fields`() = runTest {
        // Given: Response with valid and invalid games
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    [
                        {
                            "id": 1,
                            "name": "Valid Game",
                            "cover": {
                                "image_id": "valid123"
                            }
                        },
                        {
                            "id": 2,
                            "name": "Missing Cover"
                        },
                        {
                            "id": 3,
                            "cover": {
                                "image_id": "noname123"
                            }
                        },
                        {
                            "name": "Missing ID",
                            "cover": {
                                "image_id": "noid123"
                            }
                        }
                    ]
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

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh games
        repository.refreshGames(page = 1)

        // Then: Should only contain valid game (id, name, and cover present)
        repository.gamesFlow.test {
            val games = awaitItem()
            assertEquals(1, games.size, "Should filter out invalid games")
            assertEquals(1, games.first().id)
            assertEquals("Valid Game", games.first().name)
        }
    }

    @Test
    fun `caches game details after first fetch`() = runTest {
        // Given: Mock engine that counts requests
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = """
                    [
                        {
                            "id": 600,
                            "name": "Cached Game",
                            "cover": {
                                "image_id": "cached123"
                            }
                        }
                    ]
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

        val repository = GameRepositoryImpl(
            authManager = createFakeAuthManager(),
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Fetch same game twice
        val game1 = repository.getGameDetails(gameId = 600)
        val game2 = repository.getGameDetails(gameId = 600)

        // Then: Should only make 1 request (second is cached)
        assertEquals(1, requestCount)
        assertNotNull(game1)
        assertNotNull(game2)
        assertEquals(game1.id, game2.id)
    }
}
