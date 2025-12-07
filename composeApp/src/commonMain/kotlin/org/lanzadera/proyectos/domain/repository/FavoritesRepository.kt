package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem

interface FavoritesRepository {
    val favorites: Flow<List<FavoriteItem>>

    suspend fun toggleFavorite(item: FavoriteItem)
    suspend fun syncFavorites(items: List<FavoriteItem>)
}

