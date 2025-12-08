package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie

/**
 * Repository for managing movie data from TMDB API.
 *
 * Provides access to various movie categories with local caching.
 * All data is exposed via StateFlows for reactive UI updates.
 */
interface MovieRepository {
    /** Flow of now playing movies */
    val moviesFlow: StateFlow<List<Movie>>
    
    /** Flow of popular movies */
    val popularMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of top rated movies */
    val topRatedMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of upcoming movies */
    val upcomingMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of trending movies */
    val trendingMoviesFlow: StateFlow<List<Movie>>

    /**
     * Refreshes now playing movies from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshMovies(force: Boolean = false)
    
    /**
     * Refreshes popular movies from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshPopularMovies(force: Boolean = false)
    
    /**
     * Refreshes top rated movies from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTopRatedMovies(force: Boolean = false)
    
    /**
     * Refreshes upcoming movies from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshUpcomingMovies(force: Boolean = false)
    
    /**
     * Refreshes trending movies from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTrendingMovies(force: Boolean = false)
    
    /**
     * Fetches detailed information for a specific movie.
     * @param movieId The TMDB movie ID
     * @return Movie with full details, or null if not found
     */
    suspend fun getMovieDetails(movieId: Int): Movie?
}

