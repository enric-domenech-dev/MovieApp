package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.CancellationException
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.repository.FavoritesRepository
import org.lanzadera.proyectos.utils.Logger

class SyncFavoritesUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(items: List<FavoriteItem>): Result<Unit> {
        return try {
            repository.syncFavorites(items)
            Logger.d("Synced ${items.size} favorites", tag = "SyncFavoritesUseCase")
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error syncing favorites", tag = "SyncFavoritesUseCase", throwable = e)
            Result.Error(e, "Error al sincronizar favoritos")
        }
    }
}

