package org.lanzadera.proyectos.domain.usecase.games

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.repository.GameRepository
import org.lanzadera.proyectos.utils.Logger

class RefreshGamesUseCase(private val repository: GameRepository) {
    val gamesFlow: StateFlow<List<Game>> get() = repository.gamesFlow
    val popularGamesFlow: StateFlow<List<Game>> get() = repository.popularGamesFlow
    val topRatedGamesFlow: StateFlow<List<Game>> get() = repository.topRatedGamesFlow
    val upcomingGamesFlow: StateFlow<List<Game>> get() = repository.upcomingGamesFlow
    val trendingGamesFlow: StateFlow<List<Game>> get() = repository.trendingGamesFlow

    suspend operator fun invoke(): Result<Unit> {
        return try {
            repository.refreshGames()
            repository.refreshPopularGames()
            repository.refreshTopRatedGames()
            repository.refreshUpcomingGames()
            repository.refreshTrendingGames()
            Logger.d("All games refreshed successfully", tag = "RefreshGamesUseCase")
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing games", tag = "RefreshGamesUseCase", throwable = e)
            Result.Error(e, "No se pudieron actualizar los juegos")
        }
    }
}

class GetGameDetailsUseCase(private val repository: GameRepository) {
    suspend operator fun invoke(gameId: Int): Result<Game?> {
        return try {
            val game = repository.getGameDetails(gameId)
            Logger.d("Fetched details for game: $gameId", tag = "GetGameDetailsUseCase")
            Result.Success(game)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error fetching game details for: $gameId", tag = "GetGameDetailsUseCase", throwable = e)
            Result.Error(e, "No se pudieron cargar los detalles del juego")
        }
    }
}

