package org.lanzadera.proyectos.ui.models

/**
 * UI model for TV Show detail screen.
 * 
 * Contains all information needed for the detail view including
 * genres, cast, crew, seasons, episodes, networks, etc.
 * 
 * This is separate from TvShowUI (used in lists/grids) to keep that model lightweight.
 */
data class TvShowDetailUI(
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
    val tagline: String?,
    val homepage: String?,
    val type: String?,
    val originalLanguage: String?,
    val originCountry: List<String>?,
    val episodeRunTime: List<Int>?,
    val languages: List<String>?,
    
    // Complex nested objects
    val genres: List<GenreUI>?,
    val productionCompanies: List<ProductionCompanyUI>?,
    val productionCountries: List<ProductionCountryUI>?,
    val spokenLanguages: List<SpokenLanguageUI>?,
    val seasons: List<SeasonUI>?,
    val networks: List<NetworkUI>?,
    val createdBy: List<CreatedByUI>?,
    val aggregateCredits: AggregateCreditsUI?,
    val lastEpisodeToAir: EpisodeUI?,
    val nextEpisodeToAir: EpisodeUI?
) {
    val posterUrl: String
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
    
    val backdropUrl: String
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" } ?: ""
    
    val hasValidPoster: Boolean
        get() = !posterPath.isNullOrEmpty()
    
    val displayName: String
        get() = name.ifEmpty { originalName ?: "Sin título" }
    
    val seasonsInfo: String
        get() = numberOfSeasons?.let { "$it temporada${if (it > 1) "s" else ""}" } ?: ""
    
    val episodesInfo: String
        get() = numberOfEpisodes?.let { "$it episodio${if (it > 1) "s" else ""}" } ?: ""
    
    val genresText: String
        get() = genres?.joinToString(", ") { it.name } ?: ""
    
    val runtimeFormatted: String
        get() = episodeRunTime?.firstOrNull()?.let { "${it}min" } ?: "Desconocido"
}

/**
 * UI model for Season
 */
data class SeasonUI(
    val id: Int,
    val name: String,
    val seasonNumber: Int?,
    val episodeCount: Int?,
    val posterPath: String?,
    val airDate: String?,
    val overview: String?,
    val episodes: List<EpisodeUI>?
) {
    val posterUrl: String
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
}

/**
 * UI model for Episode
 */
data class EpisodeUI(
    val id: Int,
    val name: String,
    val episodeNumber: Int?,
    val seasonNumber: Int?,
    val airDate: String?,
    val overview: String?,
    val stillPath: String?,
    val voteAverage: Double?,
    val voteCount: Int?
) {
    val stillUrl: String
        get() = stillPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
    
    val episodeCode: String
        get() = "S${seasonNumber?.toString()?.padStart(2, '0')}E${episodeNumber?.toString()?.padStart(2, '0')}"
    
    val displayText: String
        get() = "$episodeCode - $name"
}

/**
 * UI model for Network
 */
data class NetworkUI(
    val id: Int,
    val name: String,
    val logoPath: String?,
    val originCountry: String?
) {
    val logoUrl: String
        get() = logoPath?.let { "https://image.tmdb.org/t/p/w185$it" } ?: ""
}

/**
 * UI model for Created By (creator/showrunner)
 */
data class CreatedByUI(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val gender: Int?
) {
    val profileUrl: String
        get() = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" } ?: ""
}
