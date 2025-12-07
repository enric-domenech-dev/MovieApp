package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.flow.first
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.repository.FavoritesRepository
import org.lanzadera.proyectos.utils.Logger
import kotlin.coroutines.cancellation.CancellationException

class ToggleBookFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(item: FavoriteItem): Result<Unit> {
        require(item.type == FavoriteType.BOOK) {
            "Item must be of type BOOK, got ${item.type}"
        }
        
        return try {
            // Toggle in simple favorites system
            favoritesRepository.toggleFavorite(item)

            // Check if now is favorite
            val isFavorite = favoritesRepository.favorites.first()
                .any { it.id == item.id && it.type == item.type }

            Logger.d(
                "Book ${item.title} is now ${if (isFavorite) "favorite" else "not favorite"}", 
                tag = "ToggleBookFavorite"
            )
            
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error toggling book favorite for ${item.title}", tag = "ToggleBookFavorite", throwable = e)
            Result.Error(e, "Could not toggle book favorite")
        }
    }
}
