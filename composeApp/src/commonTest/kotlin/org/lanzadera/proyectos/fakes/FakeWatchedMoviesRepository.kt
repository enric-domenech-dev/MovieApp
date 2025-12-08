package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.lanzadera.proyectos.domain.models.WatchedMovie
import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository

class FakeWatchedMoviesRepository : WatchedMoviesRepository {
    
    private val _watchedMovies = MutableStateFlow<List<WatchedMovie>>(emptyList())
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override fun observeAllWatchedMovies(): Flow<List<WatchedMovie>> = _watchedMovies
    
    override suspend fun toggleMovieWatched(movieId: String, isWatched: Boolean) {
        if (shouldFail) throw failureException
        
        val current = _watchedMovies.value.toMutableList()
        if (isWatched) {
            if (!current.any { it.movieId == movieId }) {
                current.add(WatchedMovie(movieId = movieId))
            }
        } else {
            current.removeAll { it.movieId == movieId }
        }
        _watchedMovies.value = current
    }
    
    override suspend fun isMovieWatched(movieId: String): Boolean {
        if (shouldFail) throw failureException
        return _watchedMovies.value.any { it.movieId == movieId }
    }
    
    fun setWatchedMovies(movieIds: Set<String>) {
        _watchedMovies.value = movieIds.map { WatchedMovie(movieId = it) }
    }
    
    fun addWatchedMovie(watchedMovie: WatchedMovie) {
        val current = _watchedMovies.value.toMutableList()
        current.add(watchedMovie)
        _watchedMovies.value = current
    }
}
