package org.lanzadera.proyectos.domain.models.movie

data class MovieWithReleaseInfo(
    val movie: Movie,
    val releaseInfo: ReleaseInfo?
)

data class ReleaseInfo(
    val releaseDate: String?,
    val isReleased: Boolean,
    val daysUntilRelease: Int?
) {
    val displayText: String
        get() = when {
            isReleased -> "Ya disponible"
            daysUntilRelease != null && daysUntilRelease == 0 -> "Se estrena hoy"
            daysUntilRelease != null && daysUntilRelease == 1 -> "Se estrena mañana"
            daysUntilRelease != null && daysUntilRelease > 1 -> "Se estrena en $daysUntilRelease días"
            else -> "Fecha de estreno desconocida"
        }
}
