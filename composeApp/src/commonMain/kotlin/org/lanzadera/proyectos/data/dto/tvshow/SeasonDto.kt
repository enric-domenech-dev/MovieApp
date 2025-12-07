package org.lanzadera.proyectos.data.dto.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDto(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("season_number") val seasonNumber: Int? = null,
    @SerialName("episode_count") val episodeCount: Int? = null,
    @SerialName("air_date") val airDate: String? = null,
    val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    val episodes: List<EpisodeDto>? = null
)
