package org.lanzadera.proyectos.data.dto.book

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponseDto(
    val kind: String? = null,
    val totalItems: Int? = null,
    val items: List<VolumeItemDto>? = null
)

@Serializable
data class VolumeItemDto(
    val id: String? = null,
    val volumeInfo: VolumeInfoDto? = null
)

@Serializable
data class VolumeInfoDto(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: ImageLinksDto? = null,
    val publishedDate: String? = null
)

@Serializable
data class ImageLinksDto(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)
