package org.lanzadera.proyectos.ui.models

/**
 * UI model for Favorite Item.
 * 
 * Simplified version of domain FavoriteItem with only fields needed for UI display.
 */
data class FavoriteItemUI(
    val id: String,
    val type: FavoriteTypeUI,
    val title: String,
    val posterUrl: String?,
    val overview: String?,
    val addedAt: Long
) {
    val addedAtText: String
        get() {
            val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(addedAt)
            return instant.toString().substringBefore('T')
        }
    
    val hasPoster: Boolean
        get() = !posterUrl.isNullOrEmpty()
}

/**
 * UI model for Favorite Type.
 */
enum class FavoriteTypeUI {
    MOVIE,
    TV_SHOW,
    BOOK,
    GAME;
    
    val displayName: String
        get() = when (this) {
            MOVIE -> "Película"
            TV_SHOW -> "Serie"
            BOOK -> "Libro"
            GAME -> "Juego"
        }
}

/**
 * UI model for FavoriteItemWithInfo.
 * Used in home screen to show favorites with additional info.
 */
sealed class FavoriteItemWithInfoUI {
    abstract val id: String
    abstract val posterUrl: String?
    abstract val updatedAt: Long
    abstract val isAvailable: Boolean
    abstract val daysUntilAvailable: Int?
    abstract val isCompleted: Boolean

    data class MovieItem(
        val movieWithRelease: MovieWithReleaseInfoUI,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfoUI() {
        override val isAvailable: Boolean
            get() = movieWithRelease.releaseInfo?.isReleased ?: false

        override val daysUntilAvailable: Int?
            get() = movieWithRelease.releaseInfo?.daysUntilRelease

        override val isCompleted: Boolean = false
    }

    data class TvShowItem(
        val tvShowWithNext: TvShowWithNextEpisodeUI,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfoUI() {
        override val isAvailable: Boolean
            get() = tvShowWithNext.nextEpisode?.isAired ?: false

        override val daysUntilAvailable: Int?
            get() = tvShowWithNext.nextEpisode?.daysUntilAir

        override val isCompleted: Boolean = false
    }

    data class WatchedMovieItem(
        val movie: MovieUI,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfoUI() {
        override val isAvailable: Boolean = true
        override val daysUntilAvailable: Int? = null
        override val isCompleted: Boolean = true
    }

    data class FinishedSeriesItem(
        val tvShow: TvShowUI,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfoUI() {
        override val isAvailable: Boolean = true
        override val daysUntilAvailable: Int? = null
        override val isCompleted: Boolean = true
    }

    data class InProductionSeriesItem(
        val tvShow: TvShowUI,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfoUI() {
        override val isAvailable: Boolean = false
        override val daysUntilAvailable: Int? = null
        override val isCompleted: Boolean = false
    }
}

/**
 * UI model for release information.
 */
data class ReleaseInfoUI(
    val releaseDate: String?,
    val isReleased: Boolean,
    val daysUntilRelease: Int?
) {
    val displayText: String
        get() = when {
            isReleased -> "Estrenada"
            daysUntilRelease != null && daysUntilRelease > 0 -> 
                "Estreno en $daysUntilRelease día${if (daysUntilRelease > 1) "s" else ""}"
            else -> "Próximamente"
        }
}

/**
 * UI model for next episode information.
 */
data class NextEpisodeInfoUI(
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val airDate: String?,
    val isAired: Boolean,
    val daysUntilAir: Int?
) {
    val episodeCode: String
        get() = "S${seasonNumber?.toString()?.padStart(2, '0')}E${episodeNumber?.toString()?.padStart(2, '0')}"
    
    val displayText: String
        get() = when {
            isAired -> "Disponible"
            daysUntilAir != null && daysUntilAir > 0 -> 
                "En $daysUntilAir día${if (daysUntilAir > 1) "s" else ""}"
            else -> "Próximamente"
        }
}
