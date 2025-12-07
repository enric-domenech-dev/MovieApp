package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.data.storage.models.StorageFavorite

class FavoritesLocalDataSourceImpl : FavoritesLocalDataSource {

    override val favorites: Flow<List<StorageFavorite>> = TODO("Not yet implemented")

    override suspend fun upsert(item: StorageFavorite) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(itemId: String, type: String) {
        TODO("Not yet implemented")
    }

    override suspend fun replaceAll(items: List<StorageFavorite>) {
        TODO("Not yet implemented")
    }

    private fun entityToStorage(entity: Any): StorageFavorite = TODO("Not yet implemented")

    private fun buildKey(id: String, type: String) = "$id-$type"
}
