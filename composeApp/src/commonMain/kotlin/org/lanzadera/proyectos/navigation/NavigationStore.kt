package org.lanzadera.proyectos.navigation

import org.lanzadera.proyectos.ui.models.BookUI

/**
 * Temporary store for Books only, as they don't have a detail endpoint.
 * 
 * ⚠️ PARTIALLY MIGRATED: Movies, TV Shows, and Games now use type-safe navigation.
 * 
 * ✅ Movies: Use Screen.MovieDetail(movieId)
 * ✅ TV Shows: Use Screen.TvShowDetail(tvShowId)
 * ✅ Games: Use Screen.GameDetail(gameId)
 * ⚠️ Books: Still use NavigationStore.selectedBook (no detail endpoint available)
 * 
 * 🔄 TODO: When Google Books API supports detail by ID, migrate to Screen.BookDetail(bookId)
 */
object NavigationStore {
    var selectedBook: BookUI? = null
}
