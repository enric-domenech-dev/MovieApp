package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.flow.first
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository
import org.lanzadera.proyectos.domain.repository.FavoritesRepository
import org.lanzadera.proyectos.domain.repository.MovieRepository
import org.lanzadera.proyectos.utils.Logger
import kotlin.coroutines.cancellation.CancellationException

/**
 * Toggles favorite status for a movie.
 *
 * When marking as favorite, fetches full movie details from TMDB
 * and saves to Room database for offline access.
 * When unmarking, removes from both favorites list and Room.
 *
 * @property favoritesRepository Repository for managing favorite IDs
 * @property favoriteDetailsRepository Repository for storing full movie details
 * @property movieRepository Repository for fetching movie data from TMDB
 */
class ToggleMovieFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository,
    private val favoriteDetailsRepository: FavoriteDetailsRepository,
    private val movieRepository: MovieRepository
) {
    /**
     * Toggles favorite status for a movie item.
     *
     * @param item The favorite item (must be of type MOVIE)
     * @return Result.Success on success, Result.Error on failure
     * @throws IllegalArgumentException if item is not of type MOVIE
     */
    suspend operator fun invoke(item: FavoriteItem): Result<Unit> {
        require(item.type == FavoriteType.MOVIE) {
            "Item must be of type MOVIE, got ${item.type}"
        }
        
        return try {
            // Toggle in simple favorites system
            favoritesRepository.toggleFavorite(item)

            // Check if now is favorite
            val isFavorite = favoritesRepository.favorites.first()
                .any { it.id == item.id && it.type == item.type }

            if (isFavorite) {
                // Get full details and save to Room
                val movieId = item.id.toIntOrNull()
                if (movieId != null) {
                    Logger.d("Saving movie $movieId with full details", tag = "ToggleMovieFavorite")
                    val details = movieRepository.getMovieDetails(movieId)
                    if (details != null) {
                        favoriteDetailsRepository.saveFavoriteMovie(details)
                        Logger.d("Movie saved - ${details.title}, date: ${details.releaseDate}", tag = "ToggleMovieFavorite")
                    } else {
                        Logger.w("Could not get details for movie $movieId", tag = "ToggleMovieFavorite")
                    }
                }
            } else {
                Logger.d("Removing movie ${item.id} from favorites", tag = "ToggleMovieFavorite")
                favoriteDetailsRepository.removeFavoriteMovie(item.id)
            }
            
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error toggling movie favorite for ${item.title}", tag = "ToggleMovieFavorite", throwable = e)
            Result.Error(e, "Could not toggle movie favorite")
        }
    }
}
