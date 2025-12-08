package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lanzadera.proyectos.data.datasource.WatchedMoviesDataSource
import org.lanzadera.proyectos.domain.models.WatchedMovie

class FakeWatchedMoviesDataSource : WatchedMoviesDataSource {
    
    private val _watchedMovies = MutableStateFlow<List<WatchedMovie>>(emptyList())
    
    override fun observeAllWatchedMovies(): Flow<List<WatchedMovie>> {
        return _watchedMovies.asStateFlow()
    }
    
    override suspend fun isMovieWatched(movieId: String): Boolean {
        return _watchedMovies.value.any { it.movieId == movieId }
    }
    
    override suspend fun markAsWatched(movie: WatchedMovie) {
        if (!_watchedMovies.value.any { it.movieId == movie.movieId }) {
            _watchedMovies.value = _watchedMovies.value + movie
        }
    }
    
    override suspend fun markAsUnwatched(movieId: String) {
        _watchedMovies.value = _watchedMovies.value.filter { it.movieId != movieId }
    }
    
    override suspend fun deleteAll() {
        _watchedMovies.value = emptyList()
    }
}
