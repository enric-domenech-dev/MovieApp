package org.lanzadera.proyectos.domain.models.book

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    val kind: String? = null,
    val totalItems: Int? = null,
    val items: List<VolumeItem>? = null
)

@Serializable
data class VolumeItem(
    val id: String? = null,
    val volumeInfo: VolumeInfo? = null
)

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null,
    val publishedDate: String? = null
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)

