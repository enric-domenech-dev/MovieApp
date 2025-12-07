package org.lanzadera.proyectos.domain.usecase.load_initial_data

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.LoadInitialData

/**
 * Use case para obtener datos iniciales ya cargados.
 * Solo expone los flows de lectura, sin permitir refresh directo.
 * El refresh es responsabilidad del SplashViewModel.
 */
class GetInitialDataUseCase(
    private val repository: LoadInitialData
) {
    // Movies flows - read only
    val moviesFlow: StateFlow<List<Movie>> get() = repository.moviesFlow
    val trendingMoviesFlow: StateFlow<List<Movie>> get() = repository.trendingMoviesFlow
    val trendingMoviesDailyFlow: StateFlow<List<Movie>> get() = repository.trendingMoviesDailyFlow
    val popularMoviesFlow: StateFlow<List<Movie>> get() = repository.popularMoviesFlow
    val topRatedMoviesFlow: StateFlow<List<Movie>> get() = repository.topRatedMoviesFlow
    val upcomingMoviesFlow: StateFlow<List<Movie>> get() = repository.upcomingMoviesFlow
    val discoverMoviesFlow: StateFlow<List<Movie>> get() = repository.discoverMoviesFlow
    val heroMoviesFlow: StateFlow<List<Movie>> get() = repository.heroMoviesFlow
    val inCinemasTodayFlow: StateFlow<List<Movie>> get() = repository.inCinemasTodayFlow
}

