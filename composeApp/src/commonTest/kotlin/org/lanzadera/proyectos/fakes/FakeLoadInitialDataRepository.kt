package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.LoadInitialData

/**
 * Fake implementation of LoadInitialData for testing.
 */
open class FakeLoadInitialDataRepository : LoadInitialData {
    
    private val _moviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _moviesFlow
    
    private val _trendingMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val trendingMoviesFlow: StateFlow<List<Movie>> = _trendingMoviesFlow
    
    private val _popularMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val popularMoviesFlow: StateFlow<List<Movie>> = _popularMoviesFlow
    
    private val _topRatedMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val topRatedMoviesFlow: StateFlow<List<Movie>> = _topRatedMoviesFlow
    
    private val _upcomingMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val upcomingMoviesFlow: StateFlow<List<Movie>> = _upcomingMoviesFlow
    
    private val _discoverMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val discoverMoviesFlow: StateFlow<List<Movie>> = _discoverMoviesFlow
    
    private val _heroMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val heroMoviesFlow: StateFlow<List<Movie>> = _heroMoviesFlow
    
    private val _trendingMoviesDailyFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val trendingMoviesDailyFlow: StateFlow<List<Movie>> = _trendingMoviesDailyFlow
    
    private val _inCinemasTodayFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val inCinemasTodayFlow: StateFlow<List<Movie>> = _inCinemasTodayFlow
    
    open override suspend fun refreshMovies(force: Boolean) {}
    open override suspend fun refreshTrendingMovies(force: Boolean) {}
    open override suspend fun refreshPopularMovies(force: Boolean) {}
    open override suspend fun refreshTopRatedMovies(force: Boolean) {}
    open override suspend fun refreshUpcomingMovies(force: Boolean) {}
    open override suspend fun refreshDiscoverMovies(force: Boolean) {}
    open override suspend fun refreshHeroMovies(force: Boolean) {}
    open override suspend fun refreshTrendingMoviesDaily(force: Boolean) {}
    open override suspend fun refreshInCinemasToday(force: Boolean) {}
    
    // Helper methods for testing
    fun addTestMovies(count: Int) {
        _moviesFlow.value = List(count) { createTestMovie(it, "Test Movie $it") }
    }
    
    fun addTestPopularMovies(count: Int) {
        _popularMoviesFlow.value = List(count) { createTestMovie(it, "Popular Movie $it") }
    }
    
    fun addTestTopRatedMovies(count: Int) {
        _topRatedMoviesFlow.value = List(count) { createTestMovie(it, "Top Rated Movie $it") }
    }
    
    private fun createTestMovie(id: Int, title: String) = Movie(
        adult = false,
        backdropPath = null,
        genreIds = emptyList(),
        id = id,
        originalLanguage = "en",
        originalTitle = title,
        overview = "Test overview",
        popularity = 10.0,
        posterPath = null,
        releaseDate = "2024-01-01",
        title = title,
        video = false
    )
}
