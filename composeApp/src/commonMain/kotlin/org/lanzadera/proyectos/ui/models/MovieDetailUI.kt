package org.lanzadera.proyectos.ui.models

/**
 * UI model for Movie detail screen.
 * 
 * Contains all information needed for the detail view including
 * genres, cast, crew, videos, budget, revenue, etc.
 * 
 * This is separate from MovieUI (used in lists/grids) to keep that model lightweight.
 */
data class MovieDetailUI(
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
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val budget: Long?,
    val revenue: Long?,
    val homepage: String?,
    val imdbId: String?,
    val originalLanguage: String?,
    val adult: Boolean?,
    val video: Boolean?,
    
    // Complex nested objects
    val genres: List<GenreUI>?,
    val productionCompanies: List<ProductionCompanyUI>?,
    val productionCountries: List<ProductionCountryUI>?,
    val spokenLanguages: List<SpokenLanguageUI>?,
    val credits: CreditsUI?,
    val videos: VideosUI?,
    val images: ImagesUI?,
    val keywords: KeywordsUI?,
    val recommendations: List<MovieUI>?,
    val similar: List<MovieUI>?,
    val belongsToCollection: CollectionUI?
) {
    val posterUrl: String
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
    
    val backdropUrl: String
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w1280$it" } ?: ""
    
    val hasValidPoster: Boolean
        get() = !posterPath.isNullOrEmpty()
    
    val displayTitle: String
        get() = title.ifEmpty { originalTitle ?: "Sin título" }
    
    val runtimeFormatted: String
        get() = runtime?.let { 
            val hours = it / 60
            val minutes = it % 60
            "${hours}h ${minutes}min"
        } ?: "Desconocido"
    
    val budgetFormatted: String
        get() = budget?.let { formatMoney(it) } ?: "N/A"
    
    val revenueFormatted: String
        get() = revenue?.let { formatMoney(it) } ?: "N/A"
    
    val genresText: String
        get() = genres?.joinToString(", ") { it.name } ?: ""
    
    private fun formatMoney(amount: Long): String {
        return when {
            amount >= 1_000_000_000 -> {
                val billions = amount / 1_000_000_000.0
                "$${billions.toString().take(3)}B"
            }
            amount >= 1_000_000 -> {
                val millions = amount / 1_000_000.0
                "$${millions.toString().take(4)}M"
            }
            else -> "$$amount"
        }
    }
}

/**
 * UI model for Genre
 */
data class GenreUI(
    val id: Int,
    val name: String
)

/**
 * UI model for Production Company
 */
data class ProductionCompanyUI(
    val id: Int,
    val name: String,
    val logoPath: String?,
    val originCountry: String?
)

/**
 * UI model for Production Country
 */
data class ProductionCountryUI(
    val iso: String,
    val name: String
)

/**
 * UI model for Spoken Language
 */
data class SpokenLanguageUI(
    val iso: String,
    val name: String,
    val englishName: String?
)

/**
 * UI model for Credits (cast and crew)
 */
data class CreditsUI(
    val cast: List<CastUI>?,
    val crew: List<CrewUI>?
)

data class CastUI(
    val id: Int,
    val name: String,
    val character: String?,
    val profilePath: String?,
    val order: Int?
) {
    val profileUrl: String
        get() = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" } ?: ""
}

data class CrewUI(
    val id: Int,
    val name: String,
    val job: String?,
    val department: String?,
    val profilePath: String?
)

/**
 * UI model for Videos
 */
data class VideosUI(
    val results: List<VideoUI>?
)

data class VideoUI(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    val official: Boolean?
) {
    val youtubeUrl: String
        get() = if (site == "YouTube") "https://www.youtube.com/watch?v=$key" else ""
}

/**
 * UI model for Images
 */
data class ImagesUI(
    val backdrops: List<ImageUI>?,
    val posters: List<ImageUI>?
)

data class ImageUI(
    val filePath: String,
    val width: Int?,
    val height: Int?
) {
    val url: String
        get() = "https://image.tmdb.org/t/p/original$filePath"
}

/**
 * UI model for Keywords
 */
data class KeywordsUI(
    val keywords: List<KeywordUI>?
)

data class KeywordUI(
    val id: Int,
    val name: String
)

/**
 * UI model for Collection
 */
data class CollectionUI(
    val id: Int,
    val name: String,
    val posterPath: String?,
    val backdropPath: String?
)
