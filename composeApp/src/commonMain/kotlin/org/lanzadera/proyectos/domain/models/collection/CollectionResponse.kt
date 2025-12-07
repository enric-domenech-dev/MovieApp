package org.lanzadera.proyectos.domain.models.collection

data class CollectionResponse(
    val page: Int,
    val results: List<Collection>,
    val totalPages: Int,
    val totalResults: Int
)

