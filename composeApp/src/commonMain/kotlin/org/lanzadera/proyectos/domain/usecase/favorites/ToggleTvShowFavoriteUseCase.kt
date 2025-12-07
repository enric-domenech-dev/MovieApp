package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.flow.first
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository
import org.lanzadera.proyectos.domain.repository.FavoritesRepository
import org.lanzadera.proyectos.domain.repository.TvShowRepository
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository
import org.lanzadera.proyectos.utils.Logger
import kotlin.coroutines.cancellation.CancellationException

class ToggleTvShowFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository,
    private val favoriteDetailsRepository: FavoriteDetailsRepository,
    private val tvShowRepository: TvShowRepository,
    private val watchedEpisodesRepository: WatchedEpisodesRepository
) {
    suspend operator fun invoke(item: FavoriteItem): Result<Unit> {
        require(item.type == FavoriteType.TV_SHOW) {
            "Item must be of type TV_SHOW, got ${item.type}"
        }
        
        return try {
            // Toggle in simple favorites system
            favoritesRepository.toggleFavorite(item)

            // Check if now is favorite
            val isFavorite = favoritesRepository.favorites.first()
                .any { it.id == item.id && it.type == item.type }

            if (isFavorite) {
                // Get full details and save to Room
                val tvShowId = item.id.toIntOrNull()
                if (tvShowId != null) {
                    Logger.d("Saving TV show $tvShowId with full details", tag = "ToggleTvShowFavorite")
                    val details = tvShowRepository.getTvShowDetails(tvShowId)
                    if (details != null) {
                        favoriteDetailsRepository.saveFavoriteTvShow(details)
                        Logger.d("TV show saved - ${details.name}, episodes: ${details.numberOfEpisodes}", tag = "ToggleTvShowFavorite")
                    } else {
                        Logger.w("Could not get details for TV show $tvShowId", tag = "ToggleTvShowFavorite")
                    }
                }
            } else {
                // Remove from Room and watched episodes
                Logger.d("Removing TV show ${item.id} from favorites", tag = "ToggleTvShowFavorite")
                favoriteDetailsRepository.removeFavoriteTvShow(item.id)
                watchedEpisodesRepository.deleteAllForTvShow(item.id)
            }
            
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error toggling TV show favorite for ${item.title}", tag = "ToggleTvShowFavorite", throwable = e)
            Result.Error(e, "Could not toggle TV show favorite")
        }
    }
}
