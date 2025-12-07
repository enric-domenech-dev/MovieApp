package org.lanzadera.proyectos.domain.usecase.favorites

import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.repository.FavoritesRepository

class SyncFavoritesUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(items: List<FavoriteItem>) = repository.syncFavorites(items)
}

