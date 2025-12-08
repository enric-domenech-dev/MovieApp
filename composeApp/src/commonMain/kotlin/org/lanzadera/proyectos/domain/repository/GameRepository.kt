package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.game.Game

/**
 * Repository for managing game data from IGDB API.
 *
 * Provides access to various game categories with local caching.
 * All data is exposed via StateFlows for reactive UI updates.
 */
interface GameRepository {
    /** Flow of recently released games */
    val gamesFlow: StateFlow<List<Game>>
    
    /** Flow of popular games */
    val popularGamesFlow: StateFlow<List<Game>>
    
    /** Flow of top rated games */
    val topRatedGamesFlow: StateFlow<List<Game>>
    
    /** Flow of upcoming games */
    val upcomingGamesFlow: StateFlow<List<Game>>
    
    /** Flow of trending games */
    val trendingGamesFlow: StateFlow<List<Game>>

    /**
     * Refreshes recently released games from IGDB API.
     * @param page Page number for pagination
     */
    suspend fun refreshGames(page: Int = 1)
    
    /**
     * Refreshes popular games from IGDB API.
     * @param page Page number for pagination
     */
    suspend fun refreshPopularGames(page: Int = 1)
    
    /**
     * Refreshes top rated games from IGDB API.
     * @param page Page number for pagination
     */
    suspend fun refreshTopRatedGames(page: Int = 1)
    
    /**
     * Refreshes upcoming games from IGDB API.
     * @param page Page number for pagination
     */
    suspend fun refreshUpcomingGames(page: Int = 1)
    
    /**
     * Refreshes trending games from IGDB API.
     * @param page Page number for pagination
     */
    suspend fun refreshTrendingGames(page: Int = 1)
    
    /**
     * Fetches detailed information for a specific game.
     * @param gameId The IGDB game ID
     * @return Game with full details, or null if not found
     */
    suspend fun getGameDetails(gameId: Int): Game?
}

