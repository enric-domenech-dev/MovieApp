package org.lanzadera.proyectos.domain.models.book

data class Book(
    val id: String? = null,
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val publishedDate: String? = null
)

