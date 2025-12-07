package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.TvShowRepository

class FakeTvShowRepository : TvShowRepository {
    
    private val _tvShowsFlow = MutableStateFlow<List<TvShow>>(emptyList())
    override val tvShowsFlow: StateFlow<List<TvShow>> = _tvShowsFlow
    
    private val _popularTvShowsFlow = MutableStateFlow<List<TvShow>>(emptyList())
    override val popularTvShowsFlow: StateFlow<List<TvShow>> = _popularTvShowsFlow
    
    private val _topRatedTvShowsFlow = MutableStateFlow<List<TvShow>>(emptyList())
    override val topRatedTvShowsFlow: StateFlow<List<TvShow>> = _topRatedTvShowsFlow
    
    private val _onAirTvShowsFlow = MutableStateFlow<List<TvShow>>(emptyList())
    override val onAirTvShowsFlow: StateFlow<List<TvShow>> = _onAirTvShowsFlow
    
    private val _trendingTvShowsFlow = MutableStateFlow<List<TvShow>>(emptyList())
    override val trendingTvShowsFlow: StateFlow<List<TvShow>> = _trendingTvShowsFlow
    
    private val _airingTodayTvShowsFlow = MutableStateFlow<List<TvShow>>(emptyList())
    override val airingTodayTvShowsFlow: StateFlow<List<TvShow>> = _airingTodayTvShowsFlow
    
    private val _trendingTvShowsWeekFlow = MutableStateFlow<List<TvShow>>(emptyList())
    override val trendingTvShowsWeekFlow: StateFlow<List<TvShow>> = _trendingTvShowsWeekFlow
    
    private val tvShowDetails = mutableMapOf<Int, TvShow?>()
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override suspend fun refreshTvShows(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshPopularTvShows(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshTopRatedTvShows(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshOnAirTvShows(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshTrendingTvShows(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshAiringTodayTvShows(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshTrendingTvShowsWeek(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun getTvShowDetails(tvShowId: Int): TvShow? {
        if (shouldFail) throw failureException
        return tvShowDetails[tvShowId]
    }
    
    fun setTvShowDetails(tvShowId: Int, tvShow: TvShow?) {
        tvShowDetails[tvShowId] = tvShow
    }
}
