package org.lanzadera.proyectos.domain.models

data class WatchedEpisode(
    val tvShowId: String,
    val seasonNumber: Int,
    val episodeNumber: Int
) {
    val id: String get() = "$tvShowId-S${seasonNumber}E${episodeNumber}"
}
