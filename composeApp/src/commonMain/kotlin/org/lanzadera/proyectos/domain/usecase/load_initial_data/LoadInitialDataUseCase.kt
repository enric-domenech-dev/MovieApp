package org.lanzadera.proyectos.domain.usecase.load_initial_data

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.LoadInitialData

class LoadInitialDataUseCase(
    private val repository: LoadInitialData
) {
    val moviesFlow: StateFlow<List<Movie>> get() = repository.moviesFlow
    val trendingMoviesFlow: StateFlow<List<Movie>> get() = repository.trendingMoviesFlow

    suspend fun refreshMovies(force: Boolean = false) =
        repository.refreshMovies(force)

    suspend fun refreshTrendingMovies(force: Boolean = false) =
        repository.refreshTrendingMovies(force)
}