package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie

/**
 * Repository for loading initial movie data on app startup.
 *
 * Provides multiple movie categories with caching and refresh capabilities.
 * Used by FilmsTabViewModel to display various movie sections.
 */
interface LoadInitialData {
    /** Flow of now playing movies */
    val moviesFlow: StateFlow<List<Movie>>
    
    /** Flow of trending movies */
    val trendingMoviesFlow: StateFlow<List<Movie>>

    /** Flow of popular movies */
    val popularMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of top rated movies */
    val topRatedMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of upcoming movies */
    val upcomingMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of discover movies */
    val discoverMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of hero/featured movies */
    val heroMoviesFlow: StateFlow<List<Movie>>
    
    /** Flow of daily trending movies */
    val trendingMoviesDailyFlow: StateFlow<List<Movie>>
    
    /** Flow of movies in cinemas today */
    val inCinemasTodayFlow: StateFlow<List<Movie>>

    /**
     * Refreshes now playing movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshMovies(force: Boolean = false)
    
    /**
     * Refreshes trending movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTrendingMovies(force: Boolean = false)

    /**
     * Refreshes popular movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshPopularMovies(force: Boolean = false)
    
    /**
     * Refreshes top rated movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTopRatedMovies(force: Boolean = false)
    
    /**
     * Refreshes upcoming movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshUpcomingMovies(force: Boolean = false)
    
    /**
     * Refreshes discover movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshDiscoverMovies(force: Boolean = false)
    
    /**
     * Refreshes hero/featured movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshHeroMovies(force: Boolean = false)
    
    /**
     * Refreshes daily trending movies.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTrendingMoviesDaily(force: Boolean = false)
    
    /**
     * Refreshes movies in cinemas today.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshInCinemasToday(force: Boolean = false)
}