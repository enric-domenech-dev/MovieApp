package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.repository.GameRepository

class FakeGameRepository : GameRepository {
    
    private val _games = MutableStateFlow<List<Game>>(emptyList())
    override val gamesFlow: StateFlow<List<Game>> = _games.asStateFlow()
    
    private val _popularGames = MutableStateFlow<List<Game>>(emptyList())
    override val popularGamesFlow: StateFlow<List<Game>> = _popularGames.asStateFlow()
    
    private val _topRatedGames = MutableStateFlow<List<Game>>(emptyList())
    override val topRatedGamesFlow: StateFlow<List<Game>> = _topRatedGames.asStateFlow()
    
    private val _upcomingGames = MutableStateFlow<List<Game>>(emptyList())
    override val upcomingGamesFlow: StateFlow<List<Game>> = _upcomingGames.asStateFlow()
    
    private val _trendingGames = MutableStateFlow<List<Game>>(emptyList())
    override val trendingGamesFlow: StateFlow<List<Game>> = _trendingGames.asStateFlow()
    
    private val gameDetails = mutableMapOf<Int, Game>()
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override suspend fun refreshGames(page: Int) {
        if (shouldFail) throw failureException
        _games.value = listOf(Game(id = 1, name = "Game 1"))
    }
    
    override suspend fun refreshPopularGames(page: Int) {
        if (shouldFail) throw failureException
        _popularGames.value = listOf(Game(id = 2, name = "Popular Game"))
    }
    
    override suspend fun refreshTopRatedGames(page: Int) {
        if (shouldFail) throw failureException
        _topRatedGames.value = listOf(Game(id = 3, name = "Top Rated Game"))
    }
    
    override suspend fun refreshUpcomingGames(page: Int) {
        if (shouldFail) throw failureException
        _upcomingGames.value = listOf(Game(id = 4, name = "Upcoming Game"))
    }
    
    override suspend fun refreshTrendingGames(page: Int) {
        if (shouldFail) throw failureException
        _trendingGames.value = listOf(Game(id = 5, name = "Trending Game"))
    }
    
    override suspend fun getGameDetails(gameId: Int): Game? {
        if (shouldFail) throw failureException
        return gameDetails[gameId]
    }
    
    fun setGameDetails(gameId: Int, game: Game) {
        gameDetails[gameId] = game
    }
}
