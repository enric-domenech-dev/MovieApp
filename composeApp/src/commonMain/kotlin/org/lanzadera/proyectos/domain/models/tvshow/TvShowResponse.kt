package org.lanzadera.proyectos.domain.models.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowResponse(
    val page: Int,
    val results: List<TvShow>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

