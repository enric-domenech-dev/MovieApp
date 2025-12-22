package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItemWithInfo
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.ui.models.FavoriteItemUI
import org.lanzadera.proyectos.ui.models.FavoriteItemWithInfoUI
import org.lanzadera.proyectos.ui.models.FavoriteTypeUI
import org.lanzadera.proyectos.ui.models.MovieWithReleaseInfoUI
import org.lanzadera.proyectos.ui.models.NextEpisodeInfoUI
import org.lanzadera.proyectos.ui.models.ReleaseInfoUI
import org.lanzadera.proyectos.ui.models.TvShowWithNextEpisodeUI

/**
 * Maps domain FavoriteItem to UI FavoriteItemUI.
 */
fun FavoriteItem.toUI(): FavoriteItemUI = FavoriteItemUI(
    id = id,
    type = type.toUI(),
    title = title,
    posterUrl = org.lanzadera.proyectos.ui.utils.normalizeImageUrl(posterUrl),
    overview = overview,
    addedAt = addedAt.toEpochMilliseconds()
)

/**
 * Maps domain FavoriteType to UI FavoriteTypeUI.
 */
fun FavoriteType.toUI(): FavoriteTypeUI = when (this) {
    FavoriteType.MOVIE -> FavoriteTypeUI.MOVIE
    FavoriteType.TV_SHOW -> FavoriteTypeUI.TV_SHOW
    FavoriteType.BOOK -> FavoriteTypeUI.BOOK
    FavoriteType.GAME -> FavoriteTypeUI.GAME
}

/**
 * Maps UI FavoriteTypeUI to domain FavoriteType.
 */
fun FavoriteTypeUI.toDomain(): FavoriteType = when (this) {
    FavoriteTypeUI.MOVIE -> FavoriteType.MOVIE
    FavoriteTypeUI.TV_SHOW -> FavoriteType.TV_SHOW
    FavoriteTypeUI.BOOK -> FavoriteType.BOOK
    FavoriteTypeUI.GAME -> FavoriteType.GAME
}

/**
 * Maps list of domain FavoriteItems to UI FavoriteItemUIs.
 */
fun List<FavoriteItem>.toUI(): List<FavoriteItemUI> = map { it.toUI() }

/**
 * Maps domain FavoriteItemWithInfo to UI FavoriteItemWithInfoUI.
 */
fun FavoriteItemWithInfo.toUI(): FavoriteItemWithInfoUI = when (this) {
    is FavoriteItemWithInfo.MovieItem -> FavoriteItemWithInfoUI.MovieItem(
        movieWithRelease = MovieWithReleaseInfoUI(
            movie = movieWithRelease.movie.toUI(),
            releaseInfo = movieWithRelease.releaseInfo?.let {
                ReleaseInfoUI(
                    releaseDate = it.releaseDate,
                    isReleased = it.isReleased,
                    daysUntilRelease = it.daysUntilRelease
                )
            }
        ),
        id = id,
        posterUrl = posterUrl,
        updatedAt = updatedAt
    )
    is FavoriteItemWithInfo.TvShowItem -> FavoriteItemWithInfoUI.TvShowItem(
        tvShowWithNext = TvShowWithNextEpisodeUI(
            tvShow = tvShowWithNext.tvShow.toUI(),
            nextEpisode = tvShowWithNext.nextEpisode?.let {
                NextEpisodeInfoUI(
                    seasonNumber = it.seasonNumber,
                    episodeNumber = it.episodeNumber,
                    airDate = it.airDate,
                    isAired = it.isAired,
                    daysUntilAir = it.daysUntilAir
                )
            }
        ),
        id = id,
        posterUrl = posterUrl,
        updatedAt = updatedAt
    )
    is FavoriteItemWithInfo.WatchedMovieItem -> FavoriteItemWithInfoUI.WatchedMovieItem(
        movie = movie.toUI(),
        id = id,
        posterUrl = posterUrl,
        updatedAt = updatedAt
    )
    is FavoriteItemWithInfo.FinishedSeriesItem -> FavoriteItemWithInfoUI.FinishedSeriesItem(
        tvShow = tvShow.toUI(),
        id = id,
        posterUrl = posterUrl,
        updatedAt = updatedAt
    )

    is FavoriteItemWithInfo.InProductionSeriesItem -> FavoriteItemWithInfoUI.InProductionSeriesItem(
        tvShow = tvShow.toUI(),
        id = id,
        posterUrl = posterUrl,
        updatedAt = updatedAt
    )
}
