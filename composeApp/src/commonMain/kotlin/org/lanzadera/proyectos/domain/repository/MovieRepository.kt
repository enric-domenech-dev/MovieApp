package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie

interface MovieRepository {
    val moviesFlow: StateFlow<List<Movie>>
    val popularMoviesFlow: StateFlow<List<Movie>>
    val topRatedMoviesFlow: StateFlow<List<Movie>>
    val upcomingMoviesFlow: StateFlow<List<Movie>>
    val trendingMoviesFlow: StateFlow<List<Movie>>

    suspend fun refreshMovies(force: Boolean = false)
    suspend fun refreshPopularMovies(force: Boolean = false)
    suspend fun refreshTopRatedMovies(force: Boolean = false)
    suspend fun refreshUpcomingMovies(force: Boolean = false)
    suspend fun refreshTrendingMovies(force: Boolean = false)
    suspend fun getMovieDetails(movieId: Int): Movie?
}

