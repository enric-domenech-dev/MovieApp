package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.repository.FavoritesRepository

class ObserveFavoritesUseCase(private val repository: FavoritesRepository) {
    operator fun invoke(): Flow<List<FavoriteItem>> = repository.favorites
}

