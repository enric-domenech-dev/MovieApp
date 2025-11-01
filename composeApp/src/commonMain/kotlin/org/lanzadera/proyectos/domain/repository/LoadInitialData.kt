package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie

interface LoadInitialData {
    val moviesFlow: StateFlow<List<Movie>>
    val trendingMoviesFlow: StateFlow<List<Movie>>

    // Additional flows
    val popularMoviesFlow: StateFlow<List<Movie>>
    val topRatedMoviesFlow: StateFlow<List<Movie>>
    val upcomingMoviesFlow: StateFlow<List<Movie>>
    val discoverMoviesFlow: StateFlow<List<Movie>>
    val heroMoviesFlow: StateFlow<List<Movie>>
    val trendingMoviesDailyFlow: StateFlow<List<Movie>>
    val inCinemasTodayFlow: StateFlow<List<Movie>>

    suspend fun refreshMovies(force: Boolean = false)
    suspend fun refreshTrendingMovies(force: Boolean = false)

    // Additional refresh methods
    suspend fun refreshPopularMovies(force: Boolean = false)
    suspend fun refreshTopRatedMovies(force: Boolean = false)
    suspend fun refreshUpcomingMovies(force: Boolean = false)
    suspend fun refreshDiscoverMovies(force: Boolean = false)
    suspend fun refreshHeroMovies(force: Boolean = false)
    suspend fun refreshTrendingMoviesDaily(force: Boolean = false)
    suspend fun refreshInCinemasToday(force: Boolean = false)
}