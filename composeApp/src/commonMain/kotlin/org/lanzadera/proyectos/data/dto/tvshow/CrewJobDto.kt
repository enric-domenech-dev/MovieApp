package org.lanzadera.proyectos.data.dto.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrewJobDto(
    @SerialName("credit_id") val creditId: String? = null,
    val job: String? = null,
    @SerialName("episode_count") val episodeCount: Int? = null
)
