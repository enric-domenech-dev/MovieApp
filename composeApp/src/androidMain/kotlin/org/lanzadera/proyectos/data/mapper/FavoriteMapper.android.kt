package org.lanzadera.proyectos.data.mapper

import kotlinx.datetime.Instant
import org.lanzadera.proyectos.data.storage.room.FavoriteItemDto
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType

actual class FavoriteItemDtoMapper {
    actual fun fromDto(dto: Any, baseImageUrl: String): FavoriteItem {
        require(dto is FavoriteItemDto) { "Expected FavoriteItemDto but got ${dto::class}" }

        return FavoriteItem(
            id = dto.id,
            type = FavoriteType.valueOf(dto.type),
            title = dto.title,
            posterUrl = dto.posterPath?.let { "$baseImageUrl$it" },
            overview = dto.overview,
            addedAt = Instant.fromEpochMilliseconds(dto.addedAt),
            updatedAt = Instant.fromEpochMilliseconds(dto.updatedAt)
        )
    }
}
