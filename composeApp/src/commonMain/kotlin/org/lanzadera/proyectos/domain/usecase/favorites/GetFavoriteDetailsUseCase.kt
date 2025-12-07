package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository

/**
 * Use case to get detailed information about favorite items.
 * 
 * Provides access to favorite movies and TV shows with full details,
 * including metadata needed for display in favorites screen.
 */
class GetFavoriteDetailsUseCase(
    private val repository: FavoriteDetailsRepository
) {
    /**
     * Observes favorite movies with full details.
     * 
     * @return Flow of favorite movies
     */
    fun observeFavoriteMovies(): Flow<List<Movie>> {
        return repository.observeFavoriteMovies()
    }
    
    /**
     * Observes upcoming releases from favorite movies.
     * 
     * @param today Today's date in format "YYYY-MM-DD"
     * @return Flow of upcoming favorite movies
     */
    fun observeUpcomingFavoriteMovies(today: String): Flow<List<Movie>> {
        return repository.observeUpcomingFavoriteMovies(today)
    }
    
    /**
     * Observes favorite TV shows with full details.
     * 
     * @return Flow of favorite TV shows
     */
    fun observeFavoriteTvShows(): Flow<List<TvShow>> {
        return repository.observeFavoriteTvShows()
    }
    
    /**
     * Gets details of a specific favorite movie.
     * 
     * @param movieId The movie ID
     * @return Movie details or null if not found
     */
    suspend fun getFavoriteMovie(movieId: String): Movie? {
        return repository.getFavoriteMovie(movieId)
    }
    
    /**
     * Gets details of a specific favorite TV show.
     * 
     * @param tvShowId The TV show ID
     * @return TV show details or null if not found
     */
    suspend fun getFavoriteTvShow(tvShowId: String): TvShow? {
        return repository.getFavoriteTvShow(tvShowId)
    }
}
