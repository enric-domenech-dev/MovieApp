package org.lanzadera.proyectos.ui.screens.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.flow.SharingStarted
import org.lanzadera.proyectos.domain.usecase.tvshows.RefreshTvShowsUseCase
import org.lanzadera.proyectos.ui.mapper.toUI
import org.lanzadera.proyectos.ui.models.TvShowUI
import org.lanzadera.proyectos.utils.Logger

/**
 * ViewModel for the Series (TV Shows) tab.
 * 
 * Manages 11 TV show sections:
 * - Base: All shows, Popular, Top Rated, On Air, Trending
 * - Additional: Airing Today, Trending Week
 * - Derived: Airing + Trending Combined, Recommended, Upcoming
 */
class SeriesTabViewModel(
    private val refreshTvShowsUseCase: RefreshTvShowsUseCase?
) : ViewModel() {

    val tvShows: StateFlow<List<TvShowUI>> = refreshTvShowsUseCase?.tvShowsFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val popularTvShows: StateFlow<List<TvShowUI>> = refreshTvShowsUseCase?.popularTvShowsFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val topRatedTvShows: StateFlow<List<TvShowUI>> = refreshTvShowsUseCase?.topRatedTvShowsFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val onAirTvShows: StateFlow<List<TvShowUI>> = refreshTvShowsUseCase?.onAirTvShowsFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val trendingTvShows: StateFlow<List<TvShowUI>> = refreshTvShowsUseCase?.trendingTvShowsFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val airingTodayTvShows: StateFlow<List<TvShowUI>> = refreshTvShowsUseCase?.airingTodayTvShowsFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val trendingTvShowsWeek: StateFlow<List<TvShowUI>> = refreshTvShowsUseCase?.trendingTvShowsWeekFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    // Derived flows (combinations)
    val airingTodayAndTrendingTvShows: StateFlow<List<TvShowUI>> =
        combine(onAirTvShows, airingTodayTvShows) { onAir, airingToday ->
            (onAir + airingToday).distinctBy { it.id }.take(20)
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val recommendedTvShows: StateFlow<List<TvShowUI>> = 
        combine(topRatedTvShows, popularTvShows) { topRated, popular ->
            (topRated + popular).distinctBy { it.id }.shuffled().take(20)
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val upcomingTvShows: StateFlow<List<TvShowUI>> = 
        tvShows.combine(onAirTvShows) { all, onAir ->
            all.filter { show -> onAir.none { it.id == show.id } }.take(20)
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        refreshIfNeeded()
    }

    /**
     * Refresh TV shows if empty (lazy loading).
     */
    private fun refreshIfNeeded() {
        if (refreshTvShowsUseCase == null || tvShows.value.isNotEmpty()) {
            Logger.d("TV shows already loaded or use case not available", tag = "SeriesTabViewModel")
            return
        }

        Logger.d("Loading TV shows for all categories", tag = "SeriesTabViewModel")
        viewModelScope.launch {
            _isRefreshing.value = true
            var hasError = false
            
            supervisorScope {
                val results = awaitAll(
                    async { refreshTvShowsUseCase.refreshTvShows(force = false) },
                    async { refreshTvShowsUseCase.refreshPopularTvShows(force = false) },
                    async { refreshTvShowsUseCase.refreshTopRatedTvShows(force = false) },
                    async { refreshTvShowsUseCase.refreshOnAirTvShows(force = false) },
                    async { refreshTvShowsUseCase.refreshTrendingTvShows(force = false) },
                    async { refreshTvShowsUseCase.refreshAiringTodayTvShows(force = false) },
                    async { refreshTvShowsUseCase.refreshTrendingTvShowsWeek(force = false) }
                )
                
                results.forEach { result ->
                    if (result is org.lanzadera.proyectos.domain.models.Result.Error) {
                        hasError = true
                        _error.value = result.message ?: "Error fetching tv shows"
                    }
                }
            }
            
            if (!hasError) {
                Logger.d("TV shows loaded successfully, total: ${tvShows.value.size}", tag = "SeriesTabViewModel")
            }
            _isRefreshing.value = false
        }
    }

    /**
     * Force refresh all TV show categories.
     */
    fun refresh() {
        if (refreshTvShowsUseCase == null) return

        Logger.d("Force refreshing TV shows", tag = "SeriesTabViewModel")
        viewModelScope.launch {
            _isRefreshing.value = true
            var hasError = false
            
            supervisorScope {
                val results = awaitAll(
                    async { refreshTvShowsUseCase.refreshTvShows(force = true) },
                    async { refreshTvShowsUseCase.refreshPopularTvShows(force = true) },
                    async { refreshTvShowsUseCase.refreshTopRatedTvShows(force = true) },
                    async { refreshTvShowsUseCase.refreshOnAirTvShows(force = true) },
                    async { refreshTvShowsUseCase.refreshTrendingTvShows(force = true) },
                    async { refreshTvShowsUseCase.refreshAiringTodayTvShows(force = true) },
                    async { refreshTvShowsUseCase.refreshTrendingTvShowsWeek(force = true) }
                )
                
                results.forEach { result ->
                    if (result is org.lanzadera.proyectos.domain.models.Result.Error) {
                        hasError = true
                        _error.value = result.message ?: "Error fetching tv shows"
                    }
                }
            }
            
            if (!hasError) {
                Logger.d("TV shows force refreshed successfully", tag = "SeriesTabViewModel")
            }
            _isRefreshing.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
