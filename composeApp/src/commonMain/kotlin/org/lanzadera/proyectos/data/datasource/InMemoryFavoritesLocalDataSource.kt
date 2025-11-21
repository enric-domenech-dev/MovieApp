package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lanzadera.proyectos.data.storage.models.StorageFavorite

class InMemoryFavoritesLocalDataSource : FavoritesLocalDataSource {
    private val favoritesState = MutableStateFlow<List<StorageFavorite>>(emptyList())
    override val favorites: Flow<List<StorageFavorite>> = favoritesState.asStateFlow()

    override suspend fun upsert(item: StorageFavorite) {
        val current = favoritesState.value.toMutableList()
        val index = current.indexOfFirst { it.id == item.id && it.type == item.type }
        if (index >= 0) {
            current[index] = item
        } else {
            current.add(item)
        }
        favoritesState.value = current
    }

    override suspend fun delete(itemId: String, type: String) {
        favoritesState.value = favoritesState.value.filterNot { it.id == itemId && it.type == type }
    }

    override suspend fun replaceAll(items: List<StorageFavorite>) {
        favoritesState.value = items
    }
}

