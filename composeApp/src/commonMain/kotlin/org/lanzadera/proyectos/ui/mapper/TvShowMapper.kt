package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.ui.models.TvShowUI

/**
 * Maps domain TvShow to UI TvShowUI.
 */
fun TvShow.toUI(): TvShowUI = TvShowUI(
    id = id ?: 0,
    name = name ?: "",
    originalName = originalName,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    firstAirDate = firstAirDate,
    lastAirDate = lastAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    numberOfSeasons = numberOfSeasons,
    numberOfEpisodes = numberOfEpisodes,
    status = status,
    inProduction = inProduction,
    genreIds = genreIds,
    tagline = tagline
)

/**
 * Maps list of domain TvShows to UI TvShowUIs.
 */
fun List<TvShow>.toUI(): List<TvShowUI> = map { it.toUI() }
