package org.lanzadera.proyectos.data.dto.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AggregateCrewDto(
    val id: Int? = null,
    val adult: Boolean? = null,
    val gender: Int? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    val popularity: Double? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    val jobs: List<CrewJobDto>? = null,
    val department: String? = null,
    @SerialName("episode_count") val episodeCount: Int? = null
)
