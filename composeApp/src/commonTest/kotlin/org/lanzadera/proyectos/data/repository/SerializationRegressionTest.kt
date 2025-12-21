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
import org.lanzadera.proyectos.data.dto.movie.MovieDto
import org.lanzadera.proyectos.data.dto.movie.MovieResponseDto
import org.lanzadera.proyectos.data.dto.tvshow.EpisodeDto
import org.lanzadera.proyectos.data.dto.tvshow.SeasonDto
import org.lanzadera.proyectos.data.mapper.toDomain
import org.lanzadera.proyectos.data.mapper.toDto
import org.lanzadera.proyectos.domain.models.tvshow.Episode
import org.lanzadera.proyectos.domain.models.tvshow.Season
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Regression tests for known serialization bugs.
 * 
 * This file documents critical bugs found during production development
 * and ensures they never happen again.
 * 
 * **CRITICAL:** These tests MUST ALWAYS PASS. If any test fails, it means
 * a regression bug has been reintroduced.
 * 
 * ## Documented Bugs:
 * 
 * ### Bug #1 (Task 1.10) - Dec 7, 2025
 * **Issue:** Season lost @Serializable during DTO migration, causing Room serialization to fail
 * **Root Cause:** Domain models had @Serializable removed but no DTO reverse mappers
 * **Impact:** Favorites tab crashed when saving TV shows with seasons
 * **Prevention:** Test Season → DTO → JSON → Room serialization
 * 
 * ### Bug #2 (Task 1.9) - Dec 7, 2025
 * **Issue:** LoadInitialDataImpl used MovieResponse (domain) instead of MovieResponseDto
 * **Root Cause:** Forgot to update repository implementation during DTO migration
 * **Impact:** Films tab crashed on app startup
 * **Prevention:** Test HTTP deserialization with DTOs
 * 
 * ## Why Unit Tests Didn't Catch These:
 * 1. Unit tests use Fakes with in-memory lists → Don't test serialization
 * 2. Integration tests needed for HTTP/Room serialization
 * 3. DTO migration requires serialization round-trip tests
 * 
 * See: docs/analysis/Why_Tests_Didnt_Catch_Bugs.md
 */
class SerializationRegressionTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // ==========================================================================
    // REGRESSION BUG #1: Season Serialization (Task 1.10)
    // ==========================================================================

    @Test
    fun `REGRESSION Bug 1 - Season with episodes can serialize to JSON for Room`() = runTest {
        // Given: A Season with episodes (the structure that caused the bug)
        val episode = Episode(
            id = 1,
            episodeNumber = 1,
            seasonNumber = 1,
            name = "Pilot",
            overview = "The beginning",
            airDate = "2024-01-01",
            runtime = 45,
            stillPath = "/still.jpg"
        )
        val season = Season(
            id = 100,
            seasonNumber = 1,
            name = "Season 1",
            overview = "First season",
            episodes = listOf(episode),
            episodeCount = 1,
            airDate = "2024-01-01",
            posterPath = "/season1.jpg"
        )

        // When: Convert to DTO and serialize to JSON (what Room does)
        // This FAILED in Task 1.10 because Season had no @Serializable
        val seasonDto = season.toDto()
        val jsonString = json.encodeToString<SeasonDto>(seasonDto)

        // Then: Should successfully deserialize back
        val deserializedDto = json.decodeFromString<SeasonDto>(jsonString)
        val deserializedSeason = deserializedDto.toDomain()

        // Verify all data preserved
        assertEquals(season.id, deserializedSeason.id)
        assertEquals(season.seasonNumber, deserializedSeason.seasonNumber)
        assertEquals(season.name, deserializedSeason.name)
        assertNotNull(deserializedSeason.episodes)
        assertEquals(1, deserializedSeason.episodes?.size)
        assertEquals("Pilot", deserializedSeason.episodes?.first()?.name)
    }

    @Test
    fun `REGRESSION Bug 1 - TvShow with seasons can be saved to Room and retrieved`() = runTest {
        // Given: A complete TvShow with seasons and episodes
        val episode = Episode(
            id = 1,
            episodeNumber = 1,
            seasonNumber = 1,
            name = "Pilot"
        )
        val season = Season(
            seasonNumber = 1,
            episodes = listOf(episode)
        )
        val tvShow = TvShow(
            id = 1,
            name = "Breaking Bad",
            seasons = listOf(season)
        )

        // When: Simulate Room storage (convert to DTO, serialize to JSON)
        val seasonDtos = tvShow.seasons!!.map { it.toDto() }
        val seasonsJson = json.encodeToString<List<SeasonDto>>(seasonDtos)

        // Then: Should NOT throw SerializationException
        // Should successfully deserialize back
        val deserializedDtos = json.decodeFromString<List<SeasonDto>>(seasonsJson)
        val deserializedSeasons = deserializedDtos.map { it.toDomain() }

        assertNotNull(deserializedSeasons)
        assertEquals(1, deserializedSeasons.size)
        assertEquals(1, deserializedSeasons.first().episodes?.size)
    }

    @Test
    fun `REGRESSION Bug 1 - Episode serialization preserves all fields`() = runTest {
        // Given: An episode with all fields
        val episode = Episode(
            id = 123,
            episodeNumber = 5,
            seasonNumber = 2,
            name = "The One Where...",
            overview = "Something happens",
            airDate = "2024-05-15",
            runtime = 42,
            stillPath = "/still123.jpg",
            voteAverage = 8.5,
            voteCount = 1000
        )

        // When: Serialize via DTO
        val episodeDto = episode.toDto()
        val jsonString = json.encodeToString<EpisodeDto>(episodeDto)
        val deserializedDto = json.decodeFromString<EpisodeDto>(jsonString)
        val deserializedEpisode = deserializedDto.toDomain()

        // Then: All fields should be preserved (especially voteAverage!)
        assertEquals(episode.id, deserializedEpisode.id)
        assertEquals(episode.name, deserializedEpisode.name)
        assertEquals(episode.voteAverage, deserializedEpisode.voteAverage)
        assertEquals(episode.voteCount, deserializedEpisode.voteCount)
    }

    // ==========================================================================
    // REGRESSION BUG #2: LoadInitialData DTOs (Task 1.9)
    // ==========================================================================

    @Test
    fun `REGRESSION Bug 2 - LoadInitialData uses MovieResponseDto not domain MovieResponse`() = runTest {
        // Given: Mock TMDB API response (real JSON structure)
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
                                "poster_path": "/poster.jpg",
                                "backdrop_path": "/backdrop.jpg",
                                "genre_ids": [18, 53],
                                "adult": false,
                                "original_language": "en",
                                "popularity": 150.0,
                                "vote_average": 8.4,
                                "vote_count": 25000,
                                "video": false
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

        val repository = LoadInitialDataImpl(
            client = httpClient,
            maxPages = 1,
            json = json
        )

        // When: Refresh movies (this FAILED in Task 1.9)
        // Should NOT throw SerializationException
        repository.refreshMovies(force = true)

        // Then: Movies should be deserialized correctly
        val movies = repository.moviesFlow.value
        assertTrue(movies.isNotEmpty(), "Movies should be loaded")
        assertEquals(550, movies.first().id)
        assertEquals("Fight Club", movies.first().title)
    }

    @Test
    fun `REGRESSION Bug 2 - MovieResponseDto can deserialize TMDB API response`() {
        // Given: Real TMDB API response JSON
        val tmdbJson = """
            {
                "page": 1,
                "results": [
                    {
                        "id": 1,
                        "title": "Test Movie",
                        "vote_average": 7.5
                    }
                ],
                "total_pages": 100,
                "total_results": 2000
            }
        """.trimIndent()

        // When: Deserialize using MovieResponseDto (not domain MovieResponse)
        // This FAILED in Task 1.9 when we used domain model
        val responseDto = json.decodeFromString<MovieResponseDto>(tmdbJson)
        val response = responseDto.toDomain()

        // Then: Should successfully deserialize
        assertNotNull(response)
        assertEquals(1, response.page)
        assertEquals(100, response.totalPages)
        assertTrue(response.results.isNotEmpty())
        assertEquals("Test Movie", response.results.first().title)
    }

    @Test
    fun `REGRESSION Bug 2 - MovieDto preserves all critical fields`() {
        // Given: A MovieDto with all TMDB fields
        val movieDto = MovieDto(
            id = 550,
            title = "Fight Club",
            originalTitle = "Fight Club",
            overview = "A ticking-time-bomb insomniac...",
            releaseDate = "1999-10-15",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(18, 53),
            adult = false,
            originalLanguage = "en",
            popularity = 150.0,
            voteAverage = 8.4,
            voteCount = 25000,
            video = false
        )

        // When: Serialize to JSON and back
        val jsonString = json.encodeToString<MovieDto>(movieDto)
        val deserialized = json.decodeFromString<MovieDto>(jsonString)

        // Then: All fields preserved
        assertEquals(movieDto.id, deserialized.id)
        assertEquals(movieDto.title, deserialized.title)
        assertEquals(movieDto.voteAverage, deserialized.voteAverage)
        assertEquals(movieDto.genreIds, deserialized.genreIds)
    }

    // ==========================================================================
    // General Serialization Safety Tests
    // ==========================================================================

    @Test
    fun `domain models without Serializable annotation do not break DTO serialization`() {
        // Given: A MovieDto (with @Serializable)
        val movieDto = MovieDto(
            id = 1,
            title = "Test",
            voteAverage = 8.5
        )

        // When: Serialize to JSON
        val jsonString = json.encodeToString<MovieDto>(movieDto)
        val deserialized = json.decodeFromString<MovieDto>(jsonString)

        // Then: Should work fine because DTOs have @Serializable
        // Domain models don't have @Serializable and that's OK
        assertEquals(movieDto.id, deserialized.id)
        assertEquals(movieDto.title, deserialized.title)
        
        // Verify we can convert to domain
        val domainMovie = deserialized.toDomain()
        assertEquals(movieDto.id, domainMovie.id)
        assertEquals(movieDto.title, domainMovie.title)
    }

    @Test
    fun `complex nested objects serialize correctly through DTOs`() {
        // Given: Complex nested structure (Season → Episodes)
        val episodes = listOf(
            Episode(id = 1, name = "E1", episodeNumber = 1, seasonNumber = 1),
            Episode(id = 2, name = "E2", episodeNumber = 2, seasonNumber = 1),
            Episode(id = 3, name = "E3", episodeNumber = 3, seasonNumber = 1)
        )
        val season = Season(
            id = 100,
            seasonNumber = 1,
            name = "Season 1",
            episodes = episodes
        )

        // When: Serialize via DTO
        val seasonDto = season.toDto()
        val jsonString = json.encodeToString<SeasonDto>(seasonDto)
        val deserialized = json.decodeFromString<SeasonDto>(jsonString)
        val backToDomain = deserialized.toDomain()

        // Then: All nested data preserved
        assertNotNull(backToDomain.episodes)
        assertEquals(3, backToDomain.episodes?.size)
        assertEquals("E1", backToDomain.episodes?.get(0)?.name)
        assertEquals("E2", backToDomain.episodes?.get(1)?.name)
        assertEquals("E3", backToDomain.episodes?.get(2)?.name)
    }
}
