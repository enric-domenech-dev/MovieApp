package org.lanzadera.proyectos.ui.models

/**
 * UI model for TV Show.
 * 
 * Simplified version of domain TvShow with only fields needed for UI display.
 */
data class TvShowUI(
    val id: Int,
    val name: String,
    val originalName: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val firstAirDate: String?,
    val lastAirDate: String?,
    val voteAverage: String,
    val voteCount: Int?,
    val popularity: Double?,
    val numberOfSeasons: Int?,
    val numberOfEpisodes: Int?,
    val status: String?,
    val inProduction: Boolean?,
    val genreIds: List<Int>?,
    val tagline: String?
) {
    val posterUrl: String
        get() = org.lanzadera.proyectos.ui.utils.normalizeImageUrl(posterPath) ?: ""
    
    val backdropUrl: String
        get() = org.lanzadera.proyectos.ui.utils.normalizeImageUrl(backdropPath) ?: ""
    
    val hasValidPoster: Boolean
        get() = !posterPath.isNullOrEmpty()
    
    val displayName: String
        get() = name.ifEmpty { originalName ?: "Sin título" }
    
    val seasonsInfo: String
        get() = numberOfSeasons?.let { "$it temporada${if (it > 1) "s" else ""}" } ?: ""
    
    val episodesInfo: String
        get() = numberOfEpisodes?.let { "$it episodio${if (it > 1) "s" else ""}" } ?: ""
}

/**
 * UI model for TV Show with next episode information.
 */
data class TvShowWithNextEpisodeUI(
    val tvShow: TvShowUI,
    val nextEpisode: NextEpisodeInfoUI?
)
