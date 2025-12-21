package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.lanzadera.proyectos.domain.models.WatchedMovie

class InMemoryWatchedMoviesDataSource : WatchedMoviesDataSource {
    private val watchedMovies = MutableStateFlow<List<WatchedMovie>>(emptyList())

    override fun observeAllWatchedMovies(): Flow<List<WatchedMovie>> {
        return watchedMovies
    }

    override suspend fun isMovieWatched(movieId: String): Boolean {
        return watchedMovies.value.any { it.movieId == movieId }
    }

    override suspend fun markAsWatched(movie: WatchedMovie) {
        val current = watchedMovies.value.filterNot { it.movieId == movie.movieId }.toMutableList()
        current.add(movie)
        watchedMovies.value = current
    }

    override suspend fun markAsUnwatched(movieId: String) {
        watchedMovies.value = watchedMovies.value.filter { it.movieId != movieId }
    }

    override suspend fun deleteAll() {
        watchedMovies.value = emptyList()
    }
}
