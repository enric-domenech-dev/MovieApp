package org.lanzadera.proyectos.data.dto.book

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: String? = null,
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val publishedDate: String? = null
)
