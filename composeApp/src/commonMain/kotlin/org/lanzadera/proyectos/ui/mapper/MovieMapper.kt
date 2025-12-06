package org.lanzadera.proyectos.ui.mapper

import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.ui.models.MovieUI

/**
 * Maps domain Movie to UI MovieUI.
 */
fun Movie.toUI(): MovieUI = MovieUI(
    id = id ?: 0,
    title = title ?: "",
    originalTitle = originalTitle,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    genreIds = genreIds,
    runtime = runtime,
    status = status,
    tagline = tagline
)

/**
 * Maps list of domain Movies to UI MovieUIs.
 */
fun List<Movie>.toUI(): List<MovieUI> = map { it.toUI() }
