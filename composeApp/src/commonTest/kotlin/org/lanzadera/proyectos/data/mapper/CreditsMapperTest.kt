package org.lanzadera.proyectos.data.mapper

import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.dto.tvshow.AggregateCastDto
import org.lanzadera.proyectos.data.dto.tvshow.AggregateCreditsDto
import org.lanzadera.proyectos.data.dto.tvshow.AggregateCrewDto
import org.lanzadera.proyectos.data.dto.tvshow.CastRoleDto
import org.lanzadera.proyectos.data.dto.tvshow.CrewJobDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for CreditsMapper - Cast and Crew DTOs.
 * 
 * These DTOs are complex and used in both movies and TV shows.
 * Critical for proper credits display.
 */
class CreditsMapperTest {
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    @Test
    fun `AggregateCastDto can be serialized and deserialized`() {
        // Given
        val dto = AggregateCastDto(
            id = 17419,
            name = "Bryan Cranston",
            originalName = "Bryan Cranston",
            profilePath = "/7Jahy5LZX2Fo8fGJltMD89yRSBV.jpg",
            gender = 2,
            popularity = 45.6,
            knownForDepartment = "Acting",
            adult = false,
            roles = listOf(
                CastRoleDto(
                    character = "Walter White",
                    episodeCount = 62,
                    creditId = "52542282760ee313280017f9"
                )
            ),
            order = 0,
            characterName = "Walter White",
            episodeCount = 62
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<AggregateCastDto>(jsonString)
        
        // Then
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.name, decoded.name)
        assertNotNull(decoded.roles)
        assertEquals(1, decoded.roles?.size)
        assertEquals("Walter White", decoded.roles?.first()?.character)
    }
    
    @Test
    fun `AggregateCastDto to Domain mapping preserves roles`() {
        // Given
        val dto = AggregateCastDto(
            id = 17419,
            name = "Bryan Cranston",
            roles = listOf(
                CastRoleDto(character = "Walter White", episodeCount = 62)
            )
        )
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertNotNull(domain.roles)
        assertEquals(1, domain.roles?.size)
        assertEquals("Walter White", domain.roles?.first()?.character)
    }
    
    @Test
    fun `AggregateCrewDto can be serialized and deserialized`() {
        // Given
        val dto = AggregateCrewDto(
            id = 1223786,
            name = "Vince Gilligan",
            originalName = "Vince Gilligan",
            profilePath = "/wSTvJGz7QbJf1HK2Mv1Cev6W9TV.jpg",
            gender = 2,
            popularity = 10.5,
            knownForDepartment = "Writing",
            adult = false,
            jobs = listOf(
                CrewJobDto(
                    job = "Executive Producer",
                    episodeCount = 62,
                    creditId = "52542287760ee31328001af1"
                ),
                CrewJobDto(
                    job = "Creator",
                    episodeCount = 62,
                    creditId = "52542287760ee31328001af7"
                )
            ),
            episodeCount = 62,
            department = "Production"
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<AggregateCrewDto>(jsonString)
        
        // Then
        assertEquals(dto.id, decoded.id)
        assertEquals(dto.name, decoded.name)
        assertNotNull(decoded.jobs)
        assertEquals(2, decoded.jobs?.size)
        assertEquals("Executive Producer", decoded.jobs?.first()?.job)
        assertEquals("Creator", decoded.jobs?.get(1)?.job)
    }
    
    @Test
    fun `AggregateCrewDto to Domain mapping preserves jobs`() {
        // Given
        val dto = AggregateCrewDto(
            id = 1223786,
            name = "Vince Gilligan",
            department = "Production",
            jobs = listOf(
                CrewJobDto(job = "Creator", episodeCount = 62),
                CrewJobDto(job = "Executive Producer", episodeCount = 62)
            )
        )
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.department, domain.department)
        assertNotNull(domain.jobs)
        assertEquals(2, domain.jobs?.size)
    }
    
    @Test
    fun `AggregateCreditsDto can be serialized and deserialized`() {
        // Given
        val dto = AggregateCreditsDto(
            cast = listOf(
                AggregateCastDto(
                    id = 17419,
                    name = "Bryan Cranston",
                    roles = listOf(CastRoleDto(character = "Walter White"))
                )
            ),
            crew = listOf(
                AggregateCrewDto(
                    id = 1223786,
                    name = "Vince Gilligan",
                    jobs = listOf(CrewJobDto(job = "Creator"))
                )
            )
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<AggregateCreditsDto>(jsonString)
        
        // Then
        assertNotNull(decoded.cast)
        assertNotNull(decoded.crew)
        assertEquals(1, decoded.cast?.size)
        assertEquals(1, decoded.crew?.size)
    }
    
    @Test
    fun `AggregateCreditsDto to Domain mapping works`() {
        // Given
        val dto = AggregateCreditsDto(
            cast = listOf(
                AggregateCastDto(id = 1, name = "Actor 1")
            ),
            crew = listOf(
                AggregateCrewDto(id = 2, name = "Director 1")
            )
        )
        
        // When
        val domain = dto.toDomain()
        
        // Then
        assertNotNull(domain.cast)
        assertNotNull(domain.crew)
        assertEquals(1, domain.cast?.size)
        assertEquals(1, domain.crew?.size)
    }
    
    @Test
    fun `CastRoleDto can be serialized independently`() {
        // Given
        val dto = CastRoleDto(
            character = "Jesse Pinkman",
            episodeCount = 62,
            creditId = "52542282760ee313280017fb"
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<CastRoleDto>(jsonString)
        
        // Then
        assertEquals(dto.character, decoded.character)
        assertEquals(dto.episodeCount, decoded.episodeCount)
        assertEquals(dto.creditId, decoded.creditId)
    }
    
    @Test
    fun `CrewJobDto can be serialized independently`() {
        // Given
        val dto = CrewJobDto(
            job = "Director of Photography",
            episodeCount = 10,
            creditId = "credit123"
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<CrewJobDto>(jsonString)
        
        // Then
        assertEquals(dto.job, decoded.job)
        assertEquals(dto.episodeCount, decoded.episodeCount)
        assertEquals(dto.creditId, decoded.creditId)
    }
    
    @Test
    fun `Empty credits can be serialized`() {
        // Given
        val dto = AggregateCreditsDto(
            cast = emptyList(),
            crew = emptyList()
        )
        
        // When
        val jsonString = json.encodeToString(dto)
        val decoded = json.decodeFromString<AggregateCreditsDto>(jsonString)
        
        // Then
        assertNotNull(decoded.cast)
        assertNotNull(decoded.crew)
        assertEquals(0, decoded.cast?.size)
        assertEquals(0, decoded.crew?.size)
    }
}
