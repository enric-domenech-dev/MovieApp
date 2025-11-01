package org.lanzadera.proyectos.domain.usecase.load_initial_data

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.LoadInitialData

class LoadInitialDataUseCase(
    private val repository: LoadInitialData
) {
    val moviesFlow: StateFlow<List<Movie>> get() = repository.moviesFlow
    val trendingMoviesFlow: StateFlow<List<Movie>> get() = repository.trendingMoviesFlow

    // Additional flows
    val popularMoviesFlow: StateFlow<List<Movie>> get() = repository.popularMoviesFlow
    val topRatedMoviesFlow: StateFlow<List<Movie>> get() = repository.topRatedMoviesFlow
    val upcomingMoviesFlow: StateFlow<List<Movie>> get() = repository.upcomingMoviesFlow
    val discoverMoviesFlow: StateFlow<List<Movie>> get() = repository.discoverMoviesFlow
    val heroMoviesFlow: StateFlow<List<Movie>> get() = repository.heroMoviesFlow
    val trendingMoviesDailyFlow: StateFlow<List<Movie>> get() = repository.trendingMoviesDailyFlow
    val inCinemasTodayFlow: StateFlow<List<Movie>> get() = repository.inCinemasTodayFlow

    suspend fun refreshMovies(force: Boolean = false) =
        repository.refreshMovies(force)

    suspend fun refreshTrendingMovies(force: Boolean = false) =
        repository.refreshTrendingMovies(force)

    // Additional refresh methods
    suspend fun refreshPopularMovies(force: Boolean = false) =
        repository.refreshPopularMovies(force)

    suspend fun refreshTopRatedMovies(force: Boolean = false) =
        repository.refreshTopRatedMovies(force)

    suspend fun refreshUpcomingMovies(force: Boolean = false) =
        repository.refreshUpcomingMovies(force)

    suspend fun refreshDiscoverMovies(force: Boolean = false) =
        repository.refreshDiscoverMovies(force)

    suspend fun refreshHeroMovies(force: Boolean = false) =
        repository.refreshHeroMovies(force)

    suspend fun refreshTrendingMoviesDaily(force: Boolean = false) =
        repository.refreshTrendingMoviesDaily(force)

    suspend fun refreshInCinemasToday(force: Boolean = false) =
        repository.refreshInCinemasToday(force)
}