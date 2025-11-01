package org.lanzadera.proyectos.domain.usecase.tvshows

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.TvShowRepository

class RefreshTvShowsUseCase(private val repository: TvShowRepository) {
    val tvShowsFlow: StateFlow<List<TvShow>> get() = repository.tvShowsFlow
    val popularTvShowsFlow: StateFlow<List<TvShow>> get() = repository.popularTvShowsFlow
    val topRatedTvShowsFlow: StateFlow<List<TvShow>> get() = repository.topRatedTvShowsFlow
    val onAirTvShowsFlow: StateFlow<List<TvShow>> get() = repository.onAirTvShowsFlow
    val trendingTvShowsFlow: StateFlow<List<TvShow>> get() = repository.trendingTvShowsFlow
    val airingTodayTvShowsFlow: StateFlow<List<TvShow>> get() = repository.airingTodayTvShowsFlow
    val trendingTvShowsWeekFlow: StateFlow<List<TvShow>> get() = repository.trendingTvShowsWeekFlow

    suspend fun refreshTvShows(force: Boolean = false) = repository.refreshTvShows(force)
    suspend fun refreshPopularTvShows(force: Boolean = false) = repository.refreshPopularTvShows(force)
    suspend fun refreshTopRatedTvShows(force: Boolean = false) = repository.refreshTopRatedTvShows(force)
    suspend fun refreshOnAirTvShows(force: Boolean = false) = repository.refreshOnAirTvShows(force)
    suspend fun refreshTrendingTvShows(force: Boolean = false) = repository.refreshTrendingTvShows(force)
    suspend fun refreshAiringTodayTvShows(force: Boolean = false) = repository.refreshAiringTodayTvShows(force)
    suspend fun refreshTrendingTvShowsWeek(force: Boolean = false) = repository.refreshTrendingTvShowsWeek(force)
    suspend fun getTvShowDetails(tvShowId: Int): TvShow? = repository.getTvShowDetails(tvShowId)
}

