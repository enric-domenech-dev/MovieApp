package org.lanzadera.proyectos.domain.usecase.games

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.repository.GameRepository

class RefreshGamesUseCase(private val repository: GameRepository) {
    val gamesFlow: StateFlow<List<Game>> get() = repository.gamesFlow
    val popularGamesFlow: StateFlow<List<Game>> get() = repository.popularGamesFlow
    val topRatedGamesFlow: StateFlow<List<Game>> get() = repository.topRatedGamesFlow
    val upcomingGamesFlow: StateFlow<List<Game>> get() = repository.upcomingGamesFlow
    val trendingGamesFlow: StateFlow<List<Game>> get() = repository.trendingGamesFlow

    suspend operator fun invoke() {
        repository.refreshGames()
        repository.refreshPopularGames()
        repository.refreshTopRatedGames()
        repository.refreshUpcomingGames()
        repository.refreshTrendingGames()
    }
}

class GetGameDetailsUseCase(private val repository: GameRepository) {
    suspend operator fun invoke(gameId: Int) = repository.getGameDetails(gameId)
}

