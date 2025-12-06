package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository

class FakeWatchedMoviesRepository : WatchedMoviesRepository {
    
    private val _watchedMovies = MutableStateFlow<Set<String>>(emptySet())
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override fun observeWatchedMovies(): Flow<Set<String>> = _watchedMovies
    
    override suspend fun toggleMovieWatched(movieId: String, isWatched: Boolean) {
        if (shouldFail) throw failureException
        
        val current = _watchedMovies.value.toMutableSet()
        if (isWatched) {
            current.add(movieId)
        } else {
            current.remove(movieId)
        }
        _watchedMovies.value = current
    }
    
    fun isMovieWatched(movieId: String): Boolean {
        return _watchedMovies.value.contains(movieId)
    }
    
    fun setWatchedMovies(movieIds: Set<String>) {
        _watchedMovies.value = movieIds
    }
}
