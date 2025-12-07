package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

/**
 * Repository for managing TV show data from TMDB API.
 *
 * Provides access to various TV show categories with local caching.
 * All data is exposed via StateFlows for reactive UI updates.
 */
interface TvShowRepository {
    /** Flow of currently airing TV shows */
    val tvShowsFlow: StateFlow<List<TvShow>>
    
    /** Flow of popular TV shows */
    val popularTvShowsFlow: StateFlow<List<TvShow>>
    
    /** Flow of top rated TV shows */
    val topRatedTvShowsFlow: StateFlow<List<TvShow>>
    
    /** Flow of TV shows currently on air */
    val onAirTvShowsFlow: StateFlow<List<TvShow>>
    
    /** Flow of trending TV shows (today) */
    val trendingTvShowsFlow: StateFlow<List<TvShow>>
    
    /** Flow of TV shows airing today */
    val airingTodayTvShowsFlow: StateFlow<List<TvShow>>
    
    /** Flow of trending TV shows (this week) */
    val trendingTvShowsWeekFlow: StateFlow<List<TvShow>>

    /**
     * Refreshes currently airing TV shows from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTvShows(force: Boolean = false)
    
    /**
     * Refreshes popular TV shows from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshPopularTvShows(force: Boolean = false)
    
    /**
     * Refreshes top rated TV shows from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTopRatedTvShows(force: Boolean = false)
    
    /**
     * Refreshes on air TV shows from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshOnAirTvShows(force: Boolean = false)
    
    /**
     * Refreshes trending TV shows (today) from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTrendingTvShows(force: Boolean = false)
    
    /**
     * Refreshes TV shows airing today from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshAiringTodayTvShows(force: Boolean = false)
    
    /**
     * Refreshes trending TV shows (this week) from TMDB API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTrendingTvShowsWeek(force: Boolean = false)
    
    /**
     * Fetches detailed information for a specific TV show.
     * Includes all seasons and episodes.
     * @param tvShowId The TMDB TV show ID
     * @return TvShow with full details, or null if not found
     */
    suspend fun getTvShowDetails(tvShowId: Int): TvShow?
}

