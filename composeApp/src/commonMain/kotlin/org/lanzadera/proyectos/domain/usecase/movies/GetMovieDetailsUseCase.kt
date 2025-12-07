package org.lanzadera.proyectos.domain.usecase.movies

import kotlinx.coroutines.CancellationException
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.MovieRepository
import org.lanzadera.proyectos.utils.Logger

/**
 * Use case to get detailed information about a specific movie.
 * 
 * Fetches movie details including cast, crew, similar movies, etc.
 * Handles errors and returns Result type for proper error handling.
 */
class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    /**
     * Gets details for a specific movie.
     * 
     * @param movieId The movie ID
     * @return Result containing movie details or error
     */
    suspend operator fun invoke(movieId: Int): Result<Movie?> {
        return try {
            val movie = repository.getMovieDetails(movieId)
            Logger.d("Fetched details for movie: $movieId", tag = "GetMovieDetailsUseCase")
            Result.Success(movie)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error fetching movie details for: $movieId", tag = "GetMovieDetailsUseCase", throwable = e)
            Result.Error(e, "No se pudieron cargar los detalles de la película")
        }
    }
}
