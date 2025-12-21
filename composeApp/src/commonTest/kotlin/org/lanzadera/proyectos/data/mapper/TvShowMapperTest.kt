package org.lanzadera.proyectos.data.mapper

import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.tvshow.EpisodeDto
import org.lanzadera.proyectos.data.dto.tvshow.SeasonDto
import org.lanzadera.proyectos.data.dto.tvshow.TvShowDto
import org.lanzadera.proyectos.data.dto.tvshow.TvShowResponseDto
import org.lanzadera.proyectos.domain.models.tvshow.Episode
import org.lanzadera.proyectos.domain.models.tvshow.Season
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for TvShowMapper - DTO serialization and domain mapping.
 * 
 * These tests prevent bugs like:
 * - Task 1.10: Season lost @Serializable → Room serialization failed
 * - Episodes not preserved in Season → Domain mapping
 * 
 * CRITICAL: This would have caught the Bug #1 (Series not showing in favorites)
 */
class TvShowMapperTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `SeasonDto with episodes can be serialized and deserialized`() {
        // Given - This is the EXACT bug we had in Task 1.10!
        val season = SeasonDto(
            id = 1,
            seasonNumber = 1,
            name = "Season 1",
            episodeCount = 10,
            airDate = "2020-01-01",
            episodes = listOf(
                EpisodeDto(id = 1, name = "Episode 1", episodeNumber = 1, seasonNumber = 1),
                EpisodeDto(id = 2, name = "Episode 2", episodeNumber = 2, seasonNumber = 1)
            )
        )
        
        // When - Serialize to JSON (This failed silently in Bug #1)
        val jsonString = json.encodeToString(season)
        
        // Then - Deserialize back
        val decoded = json.decodeFromString<SeasonDto>(jsonString)
        
        // Verify episodes are preserved (Bug #1: episodes became null)
        assertNotNull(decoded.episodes)
        assertEquals(2, decoded.episodes?.size)
        assertEquals("Episode 1", decoded.episodes?.first()?.name)
        assertEquals("Episode 2", decoded.episodes?.get(1)?.name)
    }
    
    @Test
    fun `Season domain to DTO and back preserves episodes`() {
        // Given - Create domain Season with episodes
        val domainEpisode1 = Episode(id = 1, name = "Episode 1", episodeNumber = 1, seasonNumber = 1)
        val domainEpisode2 = Episode(id = 2, name = "Episode 2", episodeNumber = 2, seasonNumber = 1)
        val domainSeason = Season(
            id = 1,
            seasonNumber = 1,
            name = "Season 1",
            episodes = listOf(domainEpisode1, domainEpisode2)
        )
        
        // When - Convert to DTO and serialize (Room does this)
        val dto = domainSeason.toDto()
        val jsonString = json.encodeToString(dto)
        
        // Then - Deserialize and convert back to domain
        val decodedDto = json.decodeFromString<SeasonDto>(jsonString)
        val backToDomain = decodedDto.toDomain()
        
        // Verify episodes survived the round-trip (Bug #1 check)
        assertNotNull(backToDomain.episodes)
        assertEquals(2, backToDomain.episodes?.size)
        assertEquals("Episode 1", backToDomain.episodes?.first()?.name)
    }
    
    @Test
    fun `TvShowDto can be serialized and deserialized`() {
        // Given
        val dto = TvShowDto(
            id = 1396,
            name = "Breaking Bad",
            originalName = "Breaking Bad",
            overview = "A high school chemistry teacher...",
            firstAirDate = "2008-01-20",
            posterPath = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            voteAverage = 8.9,
            voteCount = 11234,
            popularity = 120.5,
            numberOfSeasons = 5,
            numberOfEpisodes = 62
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<TvShowDto>(jsonString)
        
        // Then
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.name, decoded.name)
        assertEquals(dto.voteAverage, decoded.voteAverage)
        assertEquals(dto.numberOfSeasons, decoded.numberOfSeasons)
        assertEquals(dto.numberOfEpisodes, decoded.numberOfEpisodes)
    }
    
    @Test
    fun `TvShowDto to Domain mapping preserves all fields`() {
        // Given
        val dto = TvShowDto(
            id = 1396,
            name = "Breaking Bad",
            overview = "A high school chemistry teacher...",
            firstAirDate = "2008-01-20",
            posterPath = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            voteAverage = 8.9,
            voteCount = 11234,
            numberOfSeasons = 5
        )
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.overview, domain.overview)
        assertEquals(dto.firstAirDate, domain.firstAirDate)
        assertEquals(dto.voteAverage, domain.voteAverageDouble) // Important mapping
        assertEquals(dto.numberOfSeasons, domain.numberOfSeasons)
    }
    
    @Test
    fun `TvShowResponseDto can be deserialized from JSON`() {
        // Given - Sample JSON from TMDB API
        val jsonResponse = """
            {
                "page": 1,
                "results": [
                    {
                        "id": 1396,
                        "name": "Breaking Bad",
                        "vote_average": 8.9,
                        "first_air_date": "2008-01-20"
                    }
                ],
                "total_pages": 50,
                "total_results": 1000
            }
        """.trimIndent()
        
        // When
        val response = json.decodeFromString<TvShowResponseDto>(jsonResponse)
        
        // Then
        assertEquals(1, response.page)
        assertEquals(50, response.totalPages)
        assertNotNull(response.results)
        assertEquals(1, response.results?.size)
        
        val tvShow = response.results?.first()
        assertEquals(1396, tvShow?.id)
        assertEquals("Breaking Bad", tvShow?.name)
    }
    
    @Test
    fun `EpisodeDto can be serialized and deserialized`() {
        // Given
        val dto = EpisodeDto(
            id = 12345,
            name = "Pilot",
            episodeNumber = 1,
            seasonNumber = 1,
            airDate = "2008-01-20",
            overview = "The pilot episode...",
            voteAverage = 8.5,
            runtime = 58
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<EpisodeDto>(jsonString)
        
        // Then
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.name, decoded.name)
        assertEquals(dto.episodeNumber, decoded.episodeNumber)
        assertEquals(dto.seasonNumber, decoded.seasonNumber)
        assertEquals(dto.voteAverage, decoded.voteAverage)
    }
    
    @Test
    fun `TvShow with seasons can be serialized to Room JSON`() {
        // Given - This simulates what FavoriteDetailsRepositoryImpl does
        val tvShowDto = TvShowDto(
            id = 1396,
            name = "Breaking Bad",
            seasons = listOf(
                SeasonDto(
                    seasonNumber = 1,
                    episodeCount = 7,
                    episodes = listOf(
                        EpisodeDto(id = 1, name = "Pilot", episodeNumber = 1, seasonNumber = 1)
                    )
                )
            )
        )
        
        // When - Serialize seasons to JSON (for Room storage)
        val seasonsJson = tvShowDto.seasons?.let { json.encodeToString(it) }
        
        // Then - Deserialize back
        assertNotNull(seasonsJson)
        val decodedSeasons = json.decodeFromString<List<SeasonDto>>(seasonsJson)
        assertEquals(1, decodedSeasons.size)
        assertNotNull(decodedSeasons.first().episodes)
        assertEquals(1, decodedSeasons.first().episodes?.size)
    }
    
    @Test
    fun `SeasonDto with null episodes can be serialized`() {
        // Given - Sometimes seasons don't have episodes loaded yet
        val season = SeasonDto(
            id = 1,
            seasonNumber = 1,
            name = "Season 1",
            episodes = null
        )
        
        // When
        val jsonString = json.encodeToString(season)
        val decoded = json.decodeFromString<SeasonDto>(jsonString)
        
        // Then
        assertEquals(season.id, decoded.id)
        assertEquals(season.seasonNumber, decoded.seasonNumber)
        assertEquals(null, decoded.episodes)
    }
}
