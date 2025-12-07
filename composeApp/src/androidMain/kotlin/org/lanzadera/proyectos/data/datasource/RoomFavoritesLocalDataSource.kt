package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lanzadera.proyectos.data.storage.models.StorageFavorite
import org.lanzadera.proyectos.data.storage.room.FavoriteDao
import org.lanzadera.proyectos.data.storage.room.RoomFavoriteEntity

class RoomFavoritesLocalDataSource(
    private val dao: FavoriteDao
) : FavoritesLocalDataSource {

    override val favorites: Flow<List<StorageFavorite>> = dao.observeFavorites()
        .map { list -> list.map(::entityToStorage) }

    override suspend fun upsert(item: StorageFavorite) {
        dao.upsert(item.toEntity())
    }

    override suspend fun delete(itemId: String, type: String) {
        dao.deleteByKey(buildKey(itemId, type))
    }

    override suspend fun replaceAll(items: List<StorageFavorite>) {
        dao.deleteAll()
        items.forEach { dao.upsert(it.toEntity()) }
    }

    private fun entityToStorage(entity: RoomFavoriteEntity): StorageFavorite = StorageFavorite(
        id = entity.id,
        type = entity.type,
        title = entity.title,
        posterUrl = entity.posterUrl,
        overview = entity.overview,
        addedAt = entity.addedAt,
        updatedAt = entity.updatedAt
    )

    private fun StorageFavorite.toEntity(): RoomFavoriteEntity = RoomFavoriteEntity(
        key = buildKey(id, type),
        id = id,
        type = type,
        title = title,
        posterUrl = posterUrl,
        overview = overview,
        addedAt = addedAt,
        updatedAt = updatedAt
    )

    private fun buildKey(id: String, type: String) = "$id-$type"
}

