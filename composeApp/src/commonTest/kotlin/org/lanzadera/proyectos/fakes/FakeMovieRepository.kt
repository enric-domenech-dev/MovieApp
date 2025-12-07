package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.MovieRepository

class FakeMovieRepository : MovieRepository {
    
    private val _moviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val moviesFlow: StateFlow<List<Movie>> = _moviesFlow
    
    private val _popularMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val popularMoviesFlow: StateFlow<List<Movie>> = _popularMoviesFlow
    
    private val _topRatedMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val topRatedMoviesFlow: StateFlow<List<Movie>> = _topRatedMoviesFlow
    
    private val _upcomingMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val upcomingMoviesFlow: StateFlow<List<Movie>> = _upcomingMoviesFlow
    
    private val _trendingMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    override val trendingMoviesFlow: StateFlow<List<Movie>> = _trendingMoviesFlow
    
    private val movieDetails = mutableMapOf<Int, Movie?>()
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override suspend fun refreshMovies(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshPopularMovies(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshTopRatedMovies(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshUpcomingMovies(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun refreshTrendingMovies(force: Boolean) {
        if (shouldFail) throw failureException
    }
    
    override suspend fun getMovieDetails(movieId: Int): Movie? {
        if (shouldFail) throw failureException
        return movieDetails[movieId]
    }
    
    fun setMovieDetails(movieId: Int, movie: Movie?) {
        movieDetails[movieId] = movie
    }
}
