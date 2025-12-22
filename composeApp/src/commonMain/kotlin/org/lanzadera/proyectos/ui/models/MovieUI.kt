package org.lanzadera.proyectos.ui.models

/**
 * UI model for Movie.
 * 
 * Simplified version of domain Movie with only fields needed for UI display.
 * This keeps UI layer independent from domain layer.
 */
data class MovieUI(
    val id: Int,
    val title: String,
    val originalTitle: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: String,
    val voteCount: Int?,
    val popularity: Double?,
    val genreIds: List<Int>?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?
) {
    val posterUrl: String
        get() = org.lanzadera.proyectos.ui.utils.normalizeImageUrl(posterPath) ?: ""
    
    val backdropUrl: String
        get() = org.lanzadera.proyectos.ui.utils.normalizeImageUrl(backdropPath) ?: ""
    
    val hasValidPoster: Boolean
        get() = !posterPath.isNullOrEmpty()
    
    val displayTitle: String
        get() = title.ifEmpty { originalTitle ?: "Sin título" }
}

/**
 * UI model for Movie with release information.
 */
data class MovieWithReleaseInfoUI(
    val movie: MovieUI,
    val releaseInfo: ReleaseInfoUI?
)
