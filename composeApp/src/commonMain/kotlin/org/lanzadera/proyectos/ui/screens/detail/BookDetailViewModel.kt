package org.lanzadera.proyectos.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleBookFavoriteUseCase

class BookDetailViewModel(
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleBookFavoriteUseCase: ToggleBookFavoriteUseCase
) : ViewModel() {

    val favorites: StateFlow<List<FavoriteItem>> = observeFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun toggleFavorite(book: Book) {
        val id = book.id ?: return
        val item = FavoriteItem(
            id = id,
            type = FavoriteType.BOOK,
            title = book.title.orEmpty(),
            posterUrl = book.thumbnail,
            overview = book.description
        )
        viewModelScope.launch { toggleBookFavoriteUseCase(item) }
    }
}

