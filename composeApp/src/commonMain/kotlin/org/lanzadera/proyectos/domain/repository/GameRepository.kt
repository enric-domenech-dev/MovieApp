package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.game.Game

interface GameRepository {
    val gamesFlow: StateFlow<List<Game>>
    val popularGamesFlow: StateFlow<List<Game>>
    val topRatedGamesFlow: StateFlow<List<Game>>
    val upcomingGamesFlow: StateFlow<List<Game>>
    val trendingGamesFlow: StateFlow<List<Game>>

    suspend fun refreshGames(page: Int = 1)
    suspend fun refreshPopularGames(page: Int = 1)
    suspend fun refreshTopRatedGames(page: Int = 1)
    suspend fun refreshUpcomingGames(page: Int = 1)
    suspend fun refreshTrendingGames(page: Int = 1)
    suspend fun getGameDetails(gameId: Int): Game?
}

