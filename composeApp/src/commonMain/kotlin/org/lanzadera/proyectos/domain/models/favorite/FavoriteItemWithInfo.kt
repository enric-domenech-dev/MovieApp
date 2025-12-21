package org.lanzadera.proyectos.domain.models.favorite

import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.movie.MovieWithReleaseInfo
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.models.tvshow.TvShowWithNextEpisode

sealed class FavoriteItemWithInfo {
    abstract val id: String
    abstract val posterUrl: String?
    abstract val updatedAt: Long

    abstract val isAvailable: Boolean
    abstract val daysUntilAvailable: Int?
    abstract val isCompleted: Boolean

    data class MovieItem(
        val movieWithRelease: MovieWithReleaseInfo,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfo() {
        override val isAvailable: Boolean
            get() = movieWithRelease.releaseInfo?.isReleased ?: false

        override val daysUntilAvailable: Int?
            get() = movieWithRelease.releaseInfo?.daysUntilRelease

        override val isCompleted: Boolean = false
    }

    data class TvShowItem(
        val tvShowWithNext: TvShowWithNextEpisode,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfo() {
        override val isAvailable: Boolean
            get() = tvShowWithNext.nextEpisode?.isAired ?: false

        override val daysUntilAvailable: Int?
            get() = tvShowWithNext.nextEpisode?.daysUntilAir

        override val isCompleted: Boolean = false
    }

    data class WatchedMovieItem(
        val movie: Movie,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfo() {
        override val isAvailable: Boolean = true
        override val daysUntilAvailable: Int? = null
        override val isCompleted: Boolean = true
    }

    data class FinishedSeriesItem(
        val tvShow: TvShow,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfo() {
        override val isAvailable: Boolean = true
        override val daysUntilAvailable: Int? = null
        override val isCompleted: Boolean = true
    }

    data class InProductionSeriesItem(
        val tvShow: TvShow,
        override val id: String,
        override val posterUrl: String?,
        override val updatedAt: Long
    ) : FavoriteItemWithInfo() {
        override val isAvailable: Boolean = false
        override val daysUntilAvailable: Int? = null
        override val isCompleted: Boolean = false
    }
}
