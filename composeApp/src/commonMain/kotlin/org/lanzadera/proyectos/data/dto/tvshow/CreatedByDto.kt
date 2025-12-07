package org.lanzadera.proyectos.data.dto.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatedByDto(
    val id: Int? = null,
    @SerialName("credit_id") val creditId: String? = null,
    val name: String? = null,
    val gender: Int? = null,
    @SerialName("profile_path") val profilePath: String? = null
)
