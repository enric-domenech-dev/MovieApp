package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lanzadera.proyectos.data.storage.room.FavoritesDatabase
import org.lanzadera.proyectos.data.storage.room.WatchedMovieEntity
import org.lanzadera.proyectos.domain.models.WatchedMovie

class RoomWatchedMoviesDataSource : WatchedMoviesDataSource {
    private val dao = FavoritesDatabase.instance.watchedMovieDao()

    override fun observeAllWatchedMovies(): Flow<List<WatchedMovie>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun isMovieWatched(movieId: String): Boolean {
        return dao.getByMovieId(movieId) != null
    }

    override suspend fun markAsWatched(movie: WatchedMovie) {
        dao.insert(movie.toEntity())
    }

    override suspend fun markAsUnwatched(movieId: String) {
        dao.deleteByMovieId(movieId)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    private fun WatchedMovieEntity.toDomain() = WatchedMovie(
        movieId = movieId
    )

    private fun WatchedMovie.toEntity() = WatchedMovieEntity(
        id = id,
        movieId = movieId,
        watchedAt = System.currentTimeMillis()
    )
}
