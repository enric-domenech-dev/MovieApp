package org.lanzadera.proyectos.data.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.common.GenreDto
import org.lanzadera.proyectos.data.dto.common.ProductionCompanyDto
import org.lanzadera.proyectos.data.dto.common.ProductionCountryDto
import org.lanzadera.proyectos.data.dto.common.SpokenLanguageDto
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for CommonMapper - Common DTOs used across movies/TV shows.
 * 
 * These DTOs are shared and critical for proper API deserialization.
 */
class CommonMapperTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `GenreDto can be serialized and deserialized`() {
        // Given
        val dto = GenreDto(
            id = 18,
            name = "Drama"
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<GenreDto>(jsonString)
        
        // Then
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.name, decoded.name)
    }
    
    @Test
    fun `GenreDto to Domain mapping preserves fields`() {
        // Given
        val dto = GenreDto(id = 28, name = "Action")
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
    }
    
    @Test
    fun `GenreDto list can be deserialized from JSON`() {
        // Given
        val jsonArray = """
            [
                {"id": 28, "name": "Action"},
                {"id": 35, "name": "Comedy"},
                {"id": 18, "name": "Drama"}
            ]
        """.trimIndent()
        
        // When
        val genres = json.decodeFromString<List<GenreDto>>(jsonArray)
        
        // Then
        assertEquals(3, genres.size)
        assertEquals("Action", genres[0].name)
        assertEquals("Comedy", genres[1].name)
        assertEquals("Drama", genres[2].name)
    }
    
    @Test
    fun `ProductionCompanyDto can be serialized and deserialized`() {
        // Given
        val dto = ProductionCompanyDto(
            id = 1,
            name = "Warner Bros.",
            logoPath = "/logo.png",
            originCountry = "US"
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<ProductionCompanyDto>(jsonString)
        
        // Then
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.name, decoded.name)
        assertEquals(dto.logoPath, decoded.logoPath)
        assertEquals(dto.originCountry, decoded.originCountry)
    }
    
    @Test
    fun `ProductionCompanyDto to Domain mapping works`() {
        // Given
        val dto = ProductionCompanyDto(
            id = 420,
            name = "Marvel Studios",
            logoPath = "/logo.png",
            originCountry = "US"
        )
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.logoPath, domain.logoPath)
        assertEquals(dto.originCountry, domain.originCountry)
    }
    
    @Test
    fun `ProductionCountryDto can be serialized and deserialized`() {
        // Given
        val dto = ProductionCountryDto(
            isoCode = "US",
            name = "United States of America"
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<ProductionCountryDto>(jsonString)
        
        // Then
        assertEquals(dto.isoCode, decoded.isoCode)
        assertEquals(dto.name, decoded.name)
    }
    
    @Test
    fun `ProductionCountryDto to Domain mapping works`() {
        // Given
        val dto = ProductionCountryDto(isoCode = "GB", name = "United Kingdom")
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertEquals(dto.isoCode, domain.isoCode)
        assertEquals(dto.name, domain.name)
    }
    
    @Test
    fun `SpokenLanguageDto can be serialized and deserialized`() {
        // Given
        val dto = SpokenLanguageDto(
            isoCode = "en",
            name = "English",
            englishName = "English"
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<SpokenLanguageDto>(jsonString)
        
        // Then
        assertEquals(dto.isoCode, decoded.isoCode)
        assertEquals(dto.name, decoded.name)
        assertEquals(dto.englishName, decoded.englishName)
    }
    
    @Test
    fun `SpokenLanguageDto to Domain mapping works`() {
        // Given
        val dto = SpokenLanguageDto(
            isoCode = "es",
            name = "Español",
            englishName = "Spanish"
        )
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertEquals(dto.isoCode, domain.isoCode)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.englishName, domain.englishName)
    }
    
    @Test
    fun `DTO to Domain mapping is consistent`() {
        // Given
        val genreDto = GenreDto(id = 12, name = "Adventure")
        
        // When
        val genreDomain = genreDto.toDomain()
        
        // Then - Verify mapping worked correctly
        assertEquals(genreDto.id, genreDomain.id)
        assertEquals(genreDto.name, genreDomain.name)
    }
}
