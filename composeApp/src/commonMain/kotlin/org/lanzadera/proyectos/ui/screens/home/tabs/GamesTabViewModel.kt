package org.lanzadera.proyectos.ui.screens.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.usecase.games.RefreshGamesUseCase
import org.lanzadera.proyectos.ui.mapper.toUI
import org.lanzadera.proyectos.ui.models.GameUI
import org.lanzadera.proyectos.utils.Logger

/**
 * ViewModel for the Games tab.
 * 
 * Manages 5 game sections:
 * - All games, Popular, Top Rated, Upcoming, Trending
 */
class GamesTabViewModel(
    private val refreshGamesUseCase: RefreshGamesUseCase?
) : ViewModel() {

    val games: StateFlow<List<GameUI>> = refreshGamesUseCase?.gamesFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val popularGames: StateFlow<List<GameUI>> = refreshGamesUseCase?.popularGamesFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val topRatedGames: StateFlow<List<GameUI>> = refreshGamesUseCase?.topRatedGamesFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val upcomingGames: StateFlow<List<GameUI>> = refreshGamesUseCase?.upcomingGamesFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val trendingGames: StateFlow<List<GameUI>> = refreshGamesUseCase?.trendingGamesFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        refreshIfNeeded()
    }

    /**
     * Refresh games if empty (lazy loading).
     */
    private fun refreshIfNeeded() {
        if (refreshGamesUseCase == null || games.value.isNotEmpty()) {
            Logger.d("Games already loaded or use case not available", tag = "GamesTabViewModel")
            return
        }

        Logger.d("Loading games", tag = "GamesTabViewModel")
        viewModelScope.launch {
            _isRefreshing.value = true
            when (val result = refreshGamesUseCase.invoke()) {
                is org.lanzadera.proyectos.domain.models.Result.Success -> {
                    Logger.d("Games loaded successfully, total: ${games.value.size}", tag = "GamesTabViewModel")
                }
                is org.lanzadera.proyectos.domain.models.Result.Error -> {
                    _error.value = result.message ?: "Error fetching games"
                }
                is org.lanzadera.proyectos.domain.models.Result.Loading -> {
                    // Not used in this use case
                }
            }
            _isRefreshing.value = false
        }
    }

    /**
     * Force refresh all games.
     */
    fun refresh() {
        if (refreshGamesUseCase == null) return

        Logger.d("Force refreshing games", tag = "GamesTabViewModel")
        viewModelScope.launch {
            _isRefreshing.value = true
            when (val result = refreshGamesUseCase.invoke()) {
                is org.lanzadera.proyectos.domain.models.Result.Success -> {
                    Logger.d("Games force refreshed successfully", tag = "GamesTabViewModel")
                }
                is org.lanzadera.proyectos.domain.models.Result.Error -> {
                    _error.value = result.message ?: "Error fetching games"
                }
                is org.lanzadera.proyectos.domain.models.Result.Loading -> {
                    // Not used in this use case
                }
            }
            _isRefreshing.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
