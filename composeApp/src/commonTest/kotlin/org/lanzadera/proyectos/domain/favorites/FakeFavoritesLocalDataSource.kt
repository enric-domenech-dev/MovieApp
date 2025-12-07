package org.lanzadera.proyectos.domain.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lanzadera.proyectos.data.datasource.FavoritesLocalDataSource
import org.lanzadera.proyectos.data.storage.models.StorageFavorite

class FakeFavoritesLocalDataSource : FavoritesLocalDataSource {
    private val _favorites = MutableStateFlow<List<StorageFavorite>>(emptyList())
    override val favorites: Flow<List<StorageFavorite>> = _favorites.asStateFlow()

    override suspend fun upsert(item: StorageFavorite) {
        val current = _favorites.value.toMutableList()
        val index = current.indexOfFirst { it.id == item.id && it.type == item.type }
        if (index >= 0) {
            current[index] = item
        } else {
            current.add(item)
        }
        _favorites.value = current
    }

    override suspend fun delete(itemId: String, type: String) {
        _favorites.value = _favorites.value.filterNot { it.id == itemId && it.type == type }
    }

    override suspend fun replaceAll(items: List<StorageFavorite>) {
        _favorites.value = items
    }
}

