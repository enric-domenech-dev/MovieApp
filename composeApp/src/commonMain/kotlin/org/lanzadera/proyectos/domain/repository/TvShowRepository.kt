package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

interface TvShowRepository {
    val tvShowsFlow: StateFlow<List<TvShow>>
    val popularTvShowsFlow: StateFlow<List<TvShow>>
    val topRatedTvShowsFlow: StateFlow<List<TvShow>>
    val onAirTvShowsFlow: StateFlow<List<TvShow>>
    val trendingTvShowsFlow: StateFlow<List<TvShow>>
    val airingTodayTvShowsFlow: StateFlow<List<TvShow>>
    val trendingTvShowsWeekFlow: StateFlow<List<TvShow>>

    suspend fun refreshTvShows(force: Boolean = false)
    suspend fun refreshPopularTvShows(force: Boolean = false)
    suspend fun refreshTopRatedTvShows(force: Boolean = false)
    suspend fun refreshOnAirTvShows(force: Boolean = false)
    suspend fun refreshTrendingTvShows(force: Boolean = false)
    suspend fun refreshAiringTodayTvShows(force: Boolean = false)
    suspend fun refreshTrendingTvShowsWeek(force: Boolean = false)
    suspend fun getTvShowDetails(tvShowId: Int): TvShow?
}

