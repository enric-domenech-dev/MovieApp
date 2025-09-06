package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie

interface LoadInitialData {
    val moviesFlow: StateFlow<List<Movie>>
    val trendingMoviesFlow: StateFlow<List<Movie>>

    suspend fun refreshMovies(force: Boolean = false)
    suspend fun refreshTrendingMovies(force: Boolean = false)
}
