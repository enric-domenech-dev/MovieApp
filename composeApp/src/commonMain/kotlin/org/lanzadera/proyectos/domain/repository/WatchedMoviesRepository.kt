package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedMovie

interface WatchedMoviesRepository {
    fun observeAllWatchedMovies(): Flow<List<WatchedMovie>>
    suspend fun toggleMovieWatched(movieId: String, isWatched: Boolean)
    suspend fun isMovieWatched(movieId: String): Boolean
}
