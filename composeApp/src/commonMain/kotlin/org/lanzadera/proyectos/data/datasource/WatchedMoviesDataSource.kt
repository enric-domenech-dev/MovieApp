package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedMovie

interface WatchedMoviesDataSource {
    fun observeAllWatchedMovies(): Flow<List<WatchedMovie>>
    suspend fun isMovieWatched(movieId: String): Boolean
    suspend fun markAsWatched(movie: WatchedMovie)
    suspend fun markAsUnwatched(movieId: String)
    suspend fun deleteAll()
}
