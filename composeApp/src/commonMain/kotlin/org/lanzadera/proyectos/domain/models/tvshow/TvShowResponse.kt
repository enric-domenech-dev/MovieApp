package org.lanzadera.proyectos.domain.models.tvshow

data class TvShowResponse(
    val page: Int,
    val results: List<TvShow>,
    val totalPages: Int,
    val totalResults: Int
)

