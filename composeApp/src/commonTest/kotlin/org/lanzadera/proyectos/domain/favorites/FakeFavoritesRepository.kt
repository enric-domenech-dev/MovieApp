package org.lanzadera.proyectos.domain.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.repository.FavoritesRepository

class FakeFavoritesRepository : FavoritesRepository {
    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    override val favorites: Flow<List<FavoriteItem>> = _favorites.asStateFlow()

    override suspend fun toggleFavorite(item: FavoriteItem) {
        val current = _favorites.value.toMutableList()
        val index = current.indexOfFirst { it.id == item.id && it.type == item.type }
        if (index >= 0) {
            current.removeAt(index)
        } else {
            current.add(item)
        }
        _favorites.value = current
    }

    override suspend fun syncFavorites(items: List<FavoriteItem>) {
        _favorites.value = items
    }
}

