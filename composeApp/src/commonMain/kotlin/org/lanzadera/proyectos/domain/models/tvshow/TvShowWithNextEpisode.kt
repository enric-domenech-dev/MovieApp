package org.lanzadera.proyectos.domain.models.tvshow

import org.lanzadera.proyectos.utils.DateUtils

data class TvShowWithNextEpisode(
    val tvShow: TvShow,
    val nextEpisode: NextEpisodeInfo?
)

data class NextEpisodeInfo(
    val seasonNumber: Int,
    val episodeNumber: Int,
    val episodeName: String?,
    val airDate: String?,
    val isAired: Boolean,
    val daysUntilAir: Int?
) {
    val displayText: String
        get() = when {
            isAired -> "Disponible para ver"
            daysUntilAir != null && daysUntilAir == 0 -> "Se emite hoy"
            daysUntilAir != null && daysUntilAir == 1 -> "Se emite mañana"
            daysUntilAir != null && daysUntilAir > 1 -> "Se emite en $daysUntilAir días"
            airDate != null -> DateUtils.formatDateShort(airDate, adjustForTimezone = true) ?: "Fecha desconocida"
            else -> "Fecha desconocida"
        }

    val episodeCode: String
        get() = "S${seasonNumber.toString().padStart(2, '0')}E${episodeNumber.toString().padStart(2, '0')}"
}
