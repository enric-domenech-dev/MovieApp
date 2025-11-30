package org.lanzadera.proyectos.domain.usecase.movies

import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository

class ToggleMovieWatchedUseCase(
    private val repository: WatchedMoviesRepository
) {
    suspend operator fun invoke(movieId: String, isWatched: Boolean) {
        repository.toggleMovieWatched(movieId, isWatched)
    }
}
