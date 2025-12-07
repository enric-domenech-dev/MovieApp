package org.lanzadera.proyectos.data.dto.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountryDto(
    @SerialName("iso_3166_1") val isoCode: String? = null,
    val name: String? = null
)
