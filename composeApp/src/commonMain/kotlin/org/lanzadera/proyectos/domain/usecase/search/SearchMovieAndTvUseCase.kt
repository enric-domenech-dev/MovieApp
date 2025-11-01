package org.lanzadera.proyectos.domain.usecase.search

import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

sealed class SearchResult {
    data class MovieResult(val movie: Movie) : SearchResult()
    data class TvShowResult(val tvShow: TvShow) : SearchResult()
}

interface ISearchRepository {
    suspend fun searchMovies(query: String, page: Int = 1, language: String = "en-US"): List<Movie>
    suspend fun searchTvShows(query: String, page: Int = 1, language: String = "en-US"): List<TvShow>
}

class SearchMovieAndTvUseCase(
    private val repository: ISearchRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): List<SearchResult> {
        if (query.trim().isEmpty()) {
            return emptyList()
        }

        return try {
            val movies = repository.searchMovies(query, page, language)
            val tvShows = repository.searchTvShows(query, page, language)

            // Combine results alternating between movies and TV shows
            val results = mutableListOf<SearchResult>()
            val maxSize = maxOf(movies.size, tvShows.size)

            for (i in 0 until maxSize) {
                if (i < movies.size) {
                    results.add(SearchResult.MovieResult(movies[i]))
                }
                if (i < tvShows.size) {
                    results.add(SearchResult.TvShowResult(tvShows[i]))
                }
            }

            results
        } catch (e: Exception) {
            emptyList()
        }
    }
}

