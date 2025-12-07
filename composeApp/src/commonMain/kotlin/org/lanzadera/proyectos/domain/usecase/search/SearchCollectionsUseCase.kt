package org.lanzadera.proyectos.domain.usecase.search

import kotlinx.coroutines.CancellationException
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.SearchRepository
import org.lanzadera.proyectos.utils.Logger

/**
 * Use case to search for movies.
 * 
 * Searches movies by query string and returns matching results.
 */
class SearchMoviesUseCase(
    private val searchRepository: SearchRepository
) {
    /**
     * Searches for movies matching the query.
     * 
     * @param query Search query string
     * @param page Page number for pagination
     * @param language Language code (default: en-US)
     * @return Result containing list of matching movies or error
     */
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        language: String = "en-US"
    ): Result<List<Movie>> {
        if (query.trim().isEmpty()) {
            return Result.Success(emptyList())
        }
        
        return try {
            val movies = searchRepository.searchMovies(query, page, language)
            Logger.d("Found ${movies.size} movies for query: $query", tag = "SearchMoviesUseCase")
            Result.Success(movies)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error searching movies for: $query", tag = "SearchMoviesUseCase", throwable = e)
            Result.Error(e, "Error al buscar películas")
        }
    }
}

