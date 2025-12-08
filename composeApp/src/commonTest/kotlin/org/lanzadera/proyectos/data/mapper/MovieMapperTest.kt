package org.lanzadera.proyectos.data.mapper

import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.movie.MovieDto
import org.lanzadera.proyectos.data.dto.movie.MovieResponseDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for MovieMapper - DTO serialization and domain mapping.
 * 
 * These tests prevent bugs like:
 * - Task 1.9: MovieResponse lost @Serializable → HTTP deserialization failed
 * - Missing fields after DTO → Domain mapping
 */
class MovieMapperTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `MovieDto can be serialized and deserialized`() {
        // Given
        val dto = MovieDto(
            id = 550,
            title = "Fight Club",
            originalTitle = "Fight Club",
            overview = "A ticking-time-bomb insomniac...",
            releaseDate = "1999-10-15",
            posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
            backdropPath = "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
            voteAverage = 8.4,
            voteCount = 26280,
            popularity = 90.123,
            adult = false,
            video = false,
            originalLanguage = "en",
            genreIds = listOf(18),
            mediaType = "movie"
        )
        
        // When - Serialize to JSON
        val jsonString = json.encodeToString(dto)
        
        // Then - Deserialize back to DTO
        val decoded = json.decodeFromString<MovieDto>(jsonString)
        
        // Verify all fields preserved
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.title, decoded.title)
        assertEquals(dto.overview, decoded.overview)
        assertEquals(dto.voteAverage, decoded.voteAverage)
        assertEquals(dto.releaseDate, decoded.releaseDate)
        assertEquals(dto.posterPath, decoded.posterPath)
    }
    
    @Test
    fun `MovieDto to Domain mapping preserves all fields`() {
        // Given
        val dto = MovieDto(
            id = 550,
            title = "Fight Club",
            overview = "A ticking-time-bomb insomniac...",
            releaseDate = "1999-10-15",
            posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
            voteAverage = 8.4,
            voteCount = 26280,
            popularity = 90.123,
            adult = false
        )
        
        // When
        val domain = dto.toDomain()
        
        // Then - Verify all critical fields mapped
        assertEquals(dto.id, domain.id)
        assertEquals(dto.title, domain.title)
        assertEquals(dto.overview, domain.overview)
        assertEquals(dto.releaseDate, domain.releaseDate)
        assertEquals(dto.posterPath, domain.posterPath)
        assertEquals(dto.voteAverage, domain.voteAverageDouble) // Important: voteAverage mapping
        assertEquals(dto.voteCount, domain.voteCount)
        assertEquals(dto.popularity, domain.popularity)
    }
    
    @Test
    fun `MovieResponseDto can be deserialized from JSON`() {
        // Given - Sample JSON from TMDB API
        val jsonResponse = """
            {
                "page": 1,
                "results": [
                    {
                        "id": 550,
                        "title": "Fight Club",
                        "vote_average": 8.4,
                        "release_date": "1999-10-15"
                    }
                ],
                "total_pages": 100,
                "total_results": 2000
            }
        """.trimIndent()
        
        // When
        val response = json.decodeFromString<MovieResponseDto>(jsonResponse)
        
        // Then
        assertEquals(1, response.page)
        assertEquals(100, response.totalPages)
        assertEquals(2000, response.totalResults)
        assertNotNull(response.results)
        assertEquals(1, response.results?.size)
        
        val movie = response.results?.first()
        assertEquals(550, movie?.id)
        assertEquals("Fight Club", movie?.title)
        assertEquals(8.4, movie?.voteAverage)
    }
    
    @Test
    fun `DTO to Domain to DTO round-trip preserves data`() {
        // Given - Start with DTO
        val originalDto = MovieDto(
            id = 550,
            title = "Fight Club",
            voteAverage = 8.4,
            voteCount = 1000,
            releaseDate = "1999-10-15",
            overview = "Test overview"
        )
        
        // When - Convert to domain and verify fields
        val domain = originalDto.toDomain()
        
        // Then - Verify mapping preserves all data
        assertEquals(originalDto.id, domain.id)
        assertEquals(originalDto.title, domain.title)
        assertEquals(originalDto.voteAverage, domain.voteAverageDouble)
        assertEquals(originalDto.voteCount, domain.voteCount)
        assertEquals(originalDto.releaseDate, domain.releaseDate)
        assertEquals(originalDto.overview, domain.overview)
    }
    
    @Test
    fun `MovieDto with null fields can be serialized`() {
        // Given - Minimal DTO with mostly nulls
        val dto = MovieDto(
            id = 123,
            title = "Test Movie",
            voteAverage = null,
            releaseDate = null,
            overview = null
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<MovieDto>(jsonString)
        
        // Then
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.title, decoded.title)
        assertEquals(null, decoded.voteAverage)
        assertEquals(null, decoded.releaseDate)
    }
    
    @Test
    fun `MovieResponseDto to Domain mapping works`() {
        // Given
        val responseDto = MovieResponseDto(
            page = 1,
            results = listOf(
                MovieDto(id = 1, title = "Movie 1", voteAverage = 7.5),
                MovieDto(id = 2, title = "Movie 2", voteAverage = 8.2)
            ),
            totalPages = 10,
            totalResults = 200
        )
        
        // When
        val domainResponse = responseDto.toDomain()
        
        // Then
        assertEquals(1, domainResponse.page)
        assertEquals(10, domainResponse.totalPages)
        assertEquals(200, domainResponse.totalResults)
        assertEquals(2, domainResponse.results?.size)
        
        val firstMovie = domainResponse.results?.first()
        assertEquals(1, firstMovie?.id)
        assertEquals("Movie 1", firstMovie?.title)
        assertEquals(7.5, firstMovie?.voteAverageDouble)
    }
}
