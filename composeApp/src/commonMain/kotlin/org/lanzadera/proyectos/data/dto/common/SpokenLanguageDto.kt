package org.lanzadera.proyectos.data.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguageDto(
    @SerialName("english_name") val englishName: String? = null,
    @SerialName("iso_639_1") val isoCode: String? = null,
    val name: String? = null
)
