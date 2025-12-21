package org.lanzadera.proyectos.data.mapper

import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem

actual class FavoriteItemDtoMapper {
    actual fun fromDto(dto: Any, baseImageUrl: String): FavoriteItem {
        // iOS uses in-memory storage, no DTO conversion needed
        // This is a placeholder implementation for multiplatform compilation
        throw UnsupportedOperationException("iOS platform does not use DTO mapping")
    }
}
