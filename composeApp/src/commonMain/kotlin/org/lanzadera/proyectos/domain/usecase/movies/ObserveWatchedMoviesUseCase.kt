package org.lanzadera.proyectos.domain.usecase.movies

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedMovie
import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository

/**
 * Use case to observe all watched movies.
 * 
 * Returns a Flow that emits the list of watched movies whenever it changes.
 * This allows ViewModels to reactively update UI when movies are marked as watched/unwatched.
 */
class ObserveWatchedMoviesUseCase(
    private val repository: WatchedMoviesRepository
) {
    /**
     * Observes all watched movies.
     * 
     * @return Flow of watched movies list
     */
    operator fun invoke(): Flow<List<WatchedMovie>> {
        return repository.observeAllWatchedMovies()
    }
}
