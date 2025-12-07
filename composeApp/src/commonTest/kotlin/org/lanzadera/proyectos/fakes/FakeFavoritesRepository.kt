package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.repository.FavoritesRepository

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
    
    override suspend fun syncFavorites(items: List<FavoriteItem>) {
        if (shouldFail) throw failureException
        _favorites.value = items
    }
    
    fun addFavorite(item: FavoriteItem) {
        val currentFavorites = _favorites.value.toMutableList()
        if (!currentFavorites.any { it.id == item.id && it.type == item.type }) {
            currentFavorites.add(item)
            _favorites.value = currentFavorites
        }
    }
    
    fun isFavorite(id: String): Boolean {
        return _favorites.value.any { it.id == id }
    }
}
