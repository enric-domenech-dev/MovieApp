package org.lanzadera.proyectos.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Simplified HomeViewModel - Only manages tab selection.
 * 
 * Each tab now has its own ViewModel:
 * - FavoritesTabViewModel (5 use cases, 8 flows)
 * - BooksTabViewModel (1 use case, 9 flows)
 * - FilmsTabViewModel (1 use case, 9 flows)
 * - SeriesTabViewModel (1 use case, 11 flows)
 * - GamesTabViewModel (1 use case, 5 flows)
 * 
 * This follows Single Responsibility Principle and improves:
 * - Testability (each tab tested independently)
 * - Performance (lazy loading per tab)
 * - Maintainability (smaller, focused ViewModels)
 */
class HomeViewModel : ViewModel() {

    /**
     * Home tab enum.
     */
    enum class HomeTab { FAVORITES, BOOKS, FILMS, SERIES, GAMES }

    private val _selectedTab = MutableStateFlow(HomeTab.FAVORITES)
    val selectedTab: StateFlow<HomeTab> = _selectedTab.asStateFlow()

    /**
     * Select a tab by index.
     * 
     * @param index 0=FAVORITES, 1=BOOKS, 2=FILMS, 3=SERIES, 4=GAMES
     */
    fun selectTab(index: Int) {
        _selectedTab.value = when (index) {
            0 -> HomeTab.FAVORITES
            1 -> HomeTab.BOOKS
            2 -> HomeTab.FILMS
            3 -> HomeTab.SERIES
            else -> HomeTab.GAMES
        }
    }

    /**
     * Get the current tab index.
     */
    fun getTabIndex(): Int = when (_selectedTab.value) {
        HomeTab.FAVORITES -> 0
        HomeTab.BOOKS -> 1
        HomeTab.FILMS -> 2
        HomeTab.SERIES -> 3
        HomeTab.GAMES -> 4
    }
}
