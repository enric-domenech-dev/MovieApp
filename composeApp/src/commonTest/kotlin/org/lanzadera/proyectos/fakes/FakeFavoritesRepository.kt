package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.repository.FavoritesRepository

/**
 * Fake implementation of FavoritesRepository for testing.
 */
class FakeFavoritesRepository : FavoritesRepository {
    
    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    override val favorites: StateFlow<List<FavoriteItem>> = _favorites
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override suspend fun toggleFavorite(item: FavoriteItem) {
        if (shouldFail) throw failureException
        
        val currentFavorites = _favorites.value.toMutableList()
        val existingIndex = currentFavorites.indexOfFirst { 
            it.id == item.id && it.type == item.type 
        }
        
        if (existingIndex >= 0) {
            currentFavorites.removeAt(existingIndex)
        } else {
            currentFavorites.add(item)
        }
        
        _favorites.value = currentFavorites
    }
    
    override suspend fun addFavorite(item: FavoriteItem) {
        if (shouldFail) throw failureException
        
        val currentFavorites = _favorites.value.toMutableList()
        if (!currentFavorites.any { it.id == item.id && it.type == item.type }) {
            currentFavorites.add(item)
            _favorites.value = currentFavorites
        }
    }
    
    override suspend fun removeFavorite(item: FavoriteItem) {
        if (shouldFail) throw failureException
        
        _favorites.value = _favorites.value.filter { 
            !(it.id == item.id && it.type == item.type)
        }
    }
    
    fun isFavorite(id: String): Boolean {
        return _favorites.value.any { it.id == id }
    }
    
    fun setFavorites(favorites: List<FavoriteItem>) {
        _favorites.value = favorites
    }
    
    fun clear() {
        _favorites.value = emptyList()
    }
}
