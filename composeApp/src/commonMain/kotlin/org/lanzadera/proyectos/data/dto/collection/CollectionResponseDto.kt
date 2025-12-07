package org.lanzadera.proyectos.data.dto.collection

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.lanzadera.proyectos.data.dto.movie.CollectionDto

@Serializable
data class CollectionResponseDto(
    val page: Int,
    val results: List<CollectionDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)
