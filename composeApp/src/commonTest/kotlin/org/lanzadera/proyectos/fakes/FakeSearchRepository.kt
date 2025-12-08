package org.lanzadera.proyectos.fakes

import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.SearchRepository

class FakeSearchRepository : SearchRepository {
    
    private val movieResults = mutableMapOf<String, List<Movie>>()
    private val tvShowResults = mutableMapOf<String, List<TvShow>>()
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override suspend fun searchMovies(query: String, page: Int, language: String): List<Movie> {
        if (shouldFail) throw failureException
        return movieResults[query] ?: emptyList()
    }
    
    override suspend fun searchTvShows(query: String, page: Int, language: String): List<TvShow> {
        if (shouldFail) throw failureException
        return tvShowResults[query] ?: emptyList()
    }
    
    fun setMovieResults(query: String, movies: List<Movie>) {
        movieResults[query] = movies
    }
    
    fun setTvShowResults(query: String, tvShows: List<TvShow>) {
        tvShowResults[query] = tvShows
    }
}
