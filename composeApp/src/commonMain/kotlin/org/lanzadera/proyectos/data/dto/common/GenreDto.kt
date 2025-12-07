package org.lanzadera.proyectos.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    val id: Int? = null,
    val name: String? = null
)
