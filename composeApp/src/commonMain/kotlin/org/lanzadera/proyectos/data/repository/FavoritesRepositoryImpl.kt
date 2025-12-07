package org.lanzadera.proyectos.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.lanzadera.proyectos.data.datasource.FavoritesLocalDataSource
import org.lanzadera.proyectos.data.mapper.FavoriteMapper
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.repository.FavoritesRepository

class FavoritesRepositoryImpl(
    private val localDataSource: FavoritesLocalDataSource
) : FavoritesRepository {

    override val favorites: Flow<List<FavoriteItem>> =
        localDataSource.favorites.map { list -> list.map(FavoriteMapper::fromStorage) }

    override suspend fun toggleFavorite(item: FavoriteItem) {
        val storage = FavoriteMapper.toStorage(item)
        val exists = localDataSource.favorites.first().any { existing ->
            existing.id == storage.id && existing.type == storage.type
        }
        if (exists) {
            localDataSource.delete(storage.id, storage.type)
        } else {
            localDataSource.upsert(storage)
        }
    }

    override suspend fun syncFavorites(items: List<FavoriteItem>) {
        localDataSource.replaceAll(items.map(FavoriteMapper::toStorage))
    }
}
