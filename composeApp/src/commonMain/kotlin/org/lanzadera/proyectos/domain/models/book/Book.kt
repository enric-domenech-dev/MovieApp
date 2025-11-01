package org.lanzadera.proyectos.domain.models.book

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: String? = null,
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val publishedDate: String? = null
)

