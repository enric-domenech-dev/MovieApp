package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

/**
 * Repository for managing detailed favorite items stored in Room database.
 *
 * Stores full movie and TV show details for offline access.
 * This is separate from FavoritesRepository which only stores IDs.
 */
interface FavoriteDetailsRepository {
    // TV Shows
    
    /**
     * Observes all favorite TV shows with full details.
     * @return Flow emitting list of favorite TV shows
     */
    fun observeFavoriteTvShows(): Flow<List<TvShow>>
    
    /**
     * Saves a TV show to favorites with full details.
     * @param tvShow The TV show to save
     */
    suspend fun saveFavoriteTvShow(tvShow: TvShow)
    
    /**
     * Removes a TV show from favorites.
     * @param tvShowId The TV show ID
     */
    suspend fun removeFavoriteTvShow(tvShowId: String)
    
    /**
     * Gets a favorite TV show by ID.
     * @param tvShowId The TV show ID
     * @return TV show if found, null otherwise
     */
    suspend fun getFavoriteTvShow(tvShowId: String): TvShow?

    // Movies
    
    /**
     * Observes all favorite movies with full details.
     * @return Flow emitting list of favorite movies
     */
    fun observeFavoriteMovies(): Flow<List<Movie>>
    
    /**
     * Observes upcoming favorite movies released after a date.
     * @param today Date string in format YYYY-MM-DD
     * @return Flow emitting list of upcoming favorite movies
     */
    fun observeUpcomingFavoriteMovies(today: String): Flow<List<Movie>>
    
    /**
     * Saves a movie to favorites with full details.
     * @param movie The movie to save
     */
    suspend fun saveFavoriteMovie(movie: Movie)
    
    /**
     * Removes a movie from favorites.
     * @param movieId The movie ID
     */
    suspend fun removeFavoriteMovie(movieId: String)
    
    /**
     * Gets a favorite movie by ID.
     * @param movieId The movie ID
     * @return Movie if found, null otherwise
     */
    suspend fun getFavoriteMovie(movieId: String): Movie?
}
