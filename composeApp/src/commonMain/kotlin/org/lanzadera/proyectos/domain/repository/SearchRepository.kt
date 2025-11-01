package org.lanzadera.proyectos.domain.repository

import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

interface SearchRepository {
    suspend fun searchMovies(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): List<Movie>

    suspend fun searchTvShows(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): List<TvShow>
}

