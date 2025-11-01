package org.lanzadera.proyectos.domain.models.collection

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    val adult: Boolean? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    val id: Int? = null,
    val name: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null
)

