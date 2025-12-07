package org.lanzadera.proyectos.domain.models.book

data class GoogleBooksResponse(
    val kind: String? = null,
    val totalItems: Int? = null,
    val items: List<VolumeItem>? = null
)

data class VolumeItem(
    val id: String? = null,
    val volumeInfo: VolumeInfo? = null
)

data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null,
    val publishedDate: String? = null
)

data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)

