package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.data.dto.collection.CollectionResponseDto
import org.lanzadera.proyectos.data.dto.movie.CollectionDto
import org.lanzadera.proyectos.domain.models.collection.Collection
import org.lanzadera.proyectos.domain.models.collection.CollectionResponse

// Note: CollectionDto.toDomain() is in MovieMapper.kt to avoid circular dependencies

// Collection (Domain) → CollectionDto
fun Collection.toDto(): CollectionDto = CollectionDto(
    id = id,
    name = name,
    posterPath = posterPath,
    backdropPath = backdropPath
)

// CollectionResponseDto → CollectionResponse (Domain)
fun CollectionResponseDto.toDomain(): CollectionResponse = CollectionResponse(
    page = page,
    results = results.map { 
        // Use the existing toDomain() from MovieMapper
        Collection(
            adult = it.id?.let { false },
            backdropPath = it.backdropPath,
            id = it.id,
            name = it.name,
            originalLanguage = null,
            originalName = null,
            overview = null,
            posterPath = it.posterPath
        )
    },
    totalPages = totalPages,
    totalResults = totalResults
)

// CollectionResponse (Domain) → CollectionResponseDto
fun CollectionResponse.toDto(): CollectionResponseDto = CollectionResponseDto(
    page = page,
    results = results.map { it.toDto() },
    totalPages = totalPages,
    totalResults = totalResults
)
