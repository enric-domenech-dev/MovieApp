package org.lanzadera.proyectos.domain.usecase.search

import kotlinx.coroutines.CancellationException
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.SearchRepository
import org.lanzadera.proyectos.utils.Logger

/**
 * Use case to search for TV shows.
 * 
 * Searches TV shows by query string and returns matching results.
 */
class SearchTvShowsUseCase(
    private val searchRepository: SearchRepository
) {
    /**
     * Searches for TV shows matching the query.
     * 
     * @param query Search query string
     * @param page Page number for pagination
     * @param language Language code (default: en-US)
     * @return Result containing list of matching TV shows or error
     */
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): Result<List<TvShow>> {
        if (query.trim().isEmpty()) {
            return Result.Success(emptyList())
        }
        
        return try {
            val tvShows = searchRepository.searchTvShows(query, page, language)
            Logger.d("Found ${tvShows.size} TV shows for query: $query", tag = "SearchTvShowsUseCase")
            Result.Success(tvShows)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error searching TV shows for: $query", tag = "SearchTvShowsUseCase", throwable = e)
            Result.Error(e, "Error al buscar series")
        }
    }
}
