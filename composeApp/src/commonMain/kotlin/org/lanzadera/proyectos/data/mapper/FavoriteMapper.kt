package org.lanzadera.proyectos.data.mapper

import kotlinx.datetime.Instant
import org.lanzadera.proyectos.data.storage.models.StorageFavorite
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType

object FavoriteMapper {
    fun toStorage(item: FavoriteItem): StorageFavorite = StorageFavorite(
        id = item.id,
        type = item.type.name,
        title = item.title,
        posterUrl = item.posterUrl,
        overview = item.overview,
        addedAt = item.addedAt.toEpochMilliseconds(),
        updatedAt = item.updatedAt.toEpochMilliseconds()
    )

    fun fromStorage(entity: StorageFavorite): FavoriteItem = FavoriteItem(
        id = entity.id,
        type = FavoriteType.valueOf(entity.type),
        title = entity.title,
        posterUrl = entity.posterUrl,
        overview = entity.overview,
        addedAt = Instant.fromEpochMilliseconds(entity.addedAt),
        updatedAt = Instant.fromEpochMilliseconds(entity.updatedAt)
    )
}
