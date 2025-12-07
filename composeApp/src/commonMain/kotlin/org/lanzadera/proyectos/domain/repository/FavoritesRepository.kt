package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem

/**
 * Repository for managing user favorites.
 *
 * Handles adding/removing favorites and syncing with persistent storage.
 */
interface FavoritesRepository {
    /** Flow of all favorite items */
    val favorites: Flow<List<FavoriteItem>>

    /**
     * Toggles favorite status for an item.
     * If item is favorite, removes it. If not favorite, adds it.
     * @param item The favorite item to toggle
     */
    suspend fun toggleFavorite(item: FavoriteItem)
    
    /**
     * Synchronizes favorites with a new list.
     * Replaces current favorites with provided items.
     * @param items List of favorite items to sync
     */
    suspend fun syncFavorites(items: List<FavoriteItem>)
}

