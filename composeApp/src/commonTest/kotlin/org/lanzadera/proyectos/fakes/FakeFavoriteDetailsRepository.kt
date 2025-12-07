package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository

class FakeFavoriteDetailsRepository : FavoriteDetailsRepository {
    
    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    private val _favoriteTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override fun observeFavoriteMovies(): Flow<List<Movie>> = _favoriteMovies
    
    override fun observeUpcomingFavoriteMovies(today: String): Flow<List<Movie>> = _favoriteMovies
    
    override fun observeFavoriteTvShows(): Flow<List<TvShow>> = _favoriteTvShows
    
    override suspend fun saveFavoriteMovie(movie: Movie) {
        if (shouldFail) throw failureException
        
        val current = _favoriteMovies.value.toMutableList()
        current.removeAll { it.id == movie.id }
        current.add(movie)
        _favoriteMovies.value = current
    }
    
    override suspend fun saveFavoriteTvShow(tvShow: TvShow) {
        if (shouldFail) throw failureException
        
        val current = _favoriteTvShows.value.toMutableList()
        current.removeAll { it.id == tvShow.id }
        current.add(tvShow)
        _favoriteTvShows.value = current
    }
    
    override suspend fun removeFavoriteMovie(movieId: String) {
        if (shouldFail) throw failureException
        
        _favoriteMovies.value = _favoriteMovies.value.filter { 
            it.id?.toString() != movieId 
        }
    }
    
    override suspend fun removeFavoriteTvShow(tvShowId: String) {
        if (shouldFail) throw failureException
        
        _favoriteTvShows.value = _favoriteTvShows.value.filter { 
            it.id?.toString() != tvShowId 
        }
    }
    
    override suspend fun getFavoriteMovie(movieId: String): Movie? {
        if (shouldFail) throw failureException
        return _favoriteMovies.value.find { it.id?.toString() == movieId }
    }
    
    override suspend fun getFavoriteTvShow(tvShowId: String): TvShow? {
        if (shouldFail) throw failureException
        return _favoriteTvShows.value.find { it.id?.toString() == tvShowId }
    }
}
