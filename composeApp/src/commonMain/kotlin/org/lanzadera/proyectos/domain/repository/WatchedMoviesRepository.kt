package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedMovie

/**
 * Repository for managing watched movie status.
 *
 * Tracks which movies the user has marked as watched.
 */
interface WatchedMoviesRepository {
    /**
     * Observes all watched movies.
     * @return Flow emitting list of watched movies
     */
    fun observeAllWatchedMovies(): Flow<List<WatchedMovie>>
    
    /**
     * Toggles watched status for a movie.
     * @param movieId The movie ID
     * @param isWatched True to mark as watched, false to unmark
     */
    suspend fun toggleMovieWatched(movieId: String, isWatched: Boolean)
    
    /**
     * Checks if a movie is marked as watched.
     * @param movieId The movie ID
     * @return True if watched, false otherwise
     */
    suspend fun isMovieWatched(movieId: String): Boolean
}
