package org.lanzadera.proyectos.data.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.data.datasource.WatchedMoviesDataSource
import org.lanzadera.proyectos.domain.models.WatchedMovie
import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository

class WatchedMoviesRepositoryImpl(
    private val dataSource: WatchedMoviesDataSource
) : WatchedMoviesRepository {

    override fun observeAllWatchedMovies(): Flow<List<WatchedMovie>> {
        return dataSource.observeAllWatchedMovies()
    }

    override suspend fun toggleMovieWatched(movieId: String, isWatched: Boolean) {
        if (isWatched) {
            dataSource.markAsWatched(WatchedMovie(movieId))
        } else {
            dataSource.markAsUnwatched(movieId)
        }
    }

    override suspend fun isMovieWatched(movieId: String): Boolean {
        return dataSource.isMovieWatched(movieId)
    }
}
