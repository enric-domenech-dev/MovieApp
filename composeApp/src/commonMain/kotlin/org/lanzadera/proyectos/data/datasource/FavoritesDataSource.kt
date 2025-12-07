package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.data.storage.models.StorageFavorite

interface FavoritesLocalDataSource {
    val favorites: Flow<List<StorageFavorite>>

    suspend fun upsert(item: StorageFavorite)
    suspend fun delete(itemId: String, type: String)
    suspend fun replaceAll(items: List<StorageFavorite>)
}
