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
 * Integration tests for TvShowRepositoryImpl.
 * 
 * Tests HTTP deserialization with real JSON responses from TMDB API.
 * 
 * Context: These tests would have caught the bug in Task 1.10 where 
 * Season lost @Serializable and failed to serialize to Room.
 */
class TvShowRepositoryImplTest : RepositoryTest() {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `can deserialize TV show details with seasons and episodes`() = runTest {
        // Given - Mock TV show details response with seasons
        val mockEngine = MockEngine { request ->
            when {
                request.url.encodedPath.contains("/season/") -> {
                    // Season details response
                    respond(
                        content = """
                            {
                                "id": 123,
                                "season_number": 1,
                                "name": "Season 1",
                                "overview": "First season",
                                "episodes": [
                                    {
                                        "id": 1,
                                        "episode_number": 1,
                                        "name": "Pilot",
                                        "overview": "First episode",
                                        "air_date": "2024-01-01",
                                        "vote_average": 8.5,
                                        "vote_count": 100
                                    },
                                    {
                                        "id": 2,
                                        "episode_number": 2,
                                        "name": "Episode 2",
                                        "overview": "Second episode",
                                        "air_date": "2024-01-08",
                                        "vote_average": 8.3,
                                        "vote_count": 95
                                    }
                                ]
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> {
                    // TV show details response
                    respond(
                        content = """
                            {
                                "id": 1234,
                                "name": "Test Show",
                                "original_name": "Test Show",
                                "overview": "A test TV show",
                                "first_air_date": "2024-01-01",
                                "poster_path": "/poster.jpg",
                                "backdrop_path": "/backdrop.jpg",
                                "vote_average": 8.5,
                                "vote_count": 1000,
                                "popularity": 100.0,
                                "original_language": "en",
                                "seasons": [
                                    {
                                        "id": 123,
                                        "season_number": 1,
                                        "name": "Season 1",
                                        "episode_count": 10
                                    }
                                ]
                            }
                        """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
        }
        
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        
        // When
        val repository = TvShowRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        val tvShow = repository.getTvShowDetails(1234)
        
        // Then - Verify TV show was deserialized correctly
        assertNotNull(tvShow)
        assertEquals(1234, tvShow.id)
        assertEquals("Test Show", tvShow.name)
        assertEquals(8.5, tvShow.voteAverageDouble)
        
        // Verify seasons
        assertNotNull(tvShow.seasons)
        assertTrue(tvShow.seasons!!.isNotEmpty())
        val firstSeason = tvShow.seasons!!.first()
        assertEquals(1, firstSeason.seasonNumber)
        assertEquals("Season 1", firstSeason.name)
        
        // Verify episodes
        assertNotNull(firstSeason.episodes)
        assertEquals(2, firstSeason.episodes?.size)
        val firstEpisode = firstSeason.episodes?.first()
        assertEquals(1, firstEpisode?.episodeNumber)
        assertEquals("Pilot", firstEpisode?.name)
    }
    
    @Test
    fun `can deserialize Season with episodes (Bug prevention - Task 1_10)`() = runTest {
        // Given - JSON for season with episodes
        val seasonJson = """
            {
                "id": 456,
                "season_number": 2,
                "name": "Season 2",
                "overview": "Second season",
                "air_date": "2024-02-01",
                "poster_path": "/season2.jpg",
                "episodes": [
                    {
                        "id": 10,
                        "episode_number": 1,
                        "name": "S02E01",
                        "overview": "First episode of season 2",
                        "air_date": "2024-02-01",
                        "still_path": "/still.jpg",
                        "vote_average": 8.7,
                        "vote_count": 120
                    }
                ]
            }
        """.trimIndent()
        
        // When - Direct deserialization test
        val seasonDto = json.decodeFromString<org.lanzadera.proyectos.data.dto.tvshow.SeasonDto>(seasonJson)
        
        // Then - Verify Season with episodes can be deserialized
        assertEquals(456, seasonDto.id)
        assertEquals(2, seasonDto.seasonNumber)
        assertEquals("Season 2", seasonDto.name)
        
        assertNotNull(seasonDto.episodes)
        assertEquals(1, seasonDto.episodes?.size)
        val episode = seasonDto.episodes?.first()
        assertEquals(1, episode?.episodeNumber)
        assertEquals("S02E01", episode?.name)
    }
    
    @Test
    fun `can deserialize TV show list response`() = runTest {
        // Given - Mock TV show list response
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "page": 1,
                        "results": [
                            {
                                "id": 100,
                                "name": "Show 1",
                                "original_name": "Show 1",
                                "overview": "First show",
                                "first_air_date": "2024-01-01",
                                "poster_path": "/poster1.jpg",
                                "backdrop_path": "/backdrop1.jpg",
                                "vote_average": 8.0,
                                "vote_count": 500,
                                "popularity": 50.0,
                                "original_language": "en"
                            },
                            {
                                "id": 200,
                                "name": "Show 2",
                                "original_name": "Show 2",
                                "overview": "Second show",
                                "first_air_date": "2024-02-01",
                                "poster_path": "/poster2.jpg",
                                "backdrop_path": "/backdrop2.jpg",
                                "vote_average": 7.5,
                                "vote_count": 300,
                                "popularity": 40.0,
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
        val repository = TvShowRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        repository.refreshTvShows(force = true)
        
        // Then
        val tvShows = repository.tvShowsFlow.value
        assertNotNull(tvShows)
        assertTrue(tvShows.isNotEmpty())
        assertEquals(2, tvShows.size)
        
        val firstShow = tvShows.first()
        assertEquals(100, firstShow.id)
        assertEquals("Show 1", firstShow.name)
        assertEquals(8.0, firstShow.voteAverageDouble)
    }
    
    @Test
    fun `handles TV shows with empty seasons list`() = runTest {
        // Given - TV show with no seasons
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "id": 999,
                        "name": "No Seasons Show",
                        "original_name": "No Seasons Show",
                        "overview": "Show without seasons",
                        "first_air_date": "2024-01-01",
                        "poster_path": "/poster.jpg",
                        "backdrop_path": "/backdrop.jpg",
                        "vote_average": 7.0,
                        "vote_count": 100,
                        "popularity": 20.0,
                        "original_language": "en",
                        "seasons": []
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
        val repository = TvShowRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        val tvShow = repository.getTvShowDetails(999)
        
        // Then
        assertNotNull(tvShow)
        assertEquals(999, tvShow.id)
        assertNotNull(tvShow.seasons)
        assertTrue(tvShow.seasons!!.isEmpty())
    }
    
    @Test
    fun `TvShowResponseDto matches TMDB API structure`() = runTest {
        // Given - Real TMDB API response structure
        val realApiJson = """
            {
                "page": 1,
                "results": [
                    {
                        "adult": false,
                        "backdrop_path": "/backdrop.jpg",
                        "genre_ids": [18, 80],
                        "id": 12345,
                        "origin_country": ["US"],
                        "original_language": "en",
                        "original_name": "Test Show",
                        "overview": "Test overview",
                        "popularity": 123.456,
                        "poster_path": "/poster.jpg",
                        "first_air_date": "2024-12-08",
                        "name": "Test Show",
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
        val repository = TvShowRepositoryImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )
        
        repository.refreshTvShows(force = true)
        
        // Then - Should deserialize without errors
        val tvShows = repository.tvShowsFlow.value
        assertNotNull(tvShows)
        
        if (tvShows.isNotEmpty()) {
            val tvShow = tvShows.first()
            assertEquals(12345, tvShow.id)
            assertEquals("Test Show", tvShow.name)
            assertEquals(8.5, tvShow.voteAverageDouble)
        }
    }
}
