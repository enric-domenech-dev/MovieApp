package org.lanzadera.proyectos.navigation

import org.lanzadera.proyectos.ui.models.BookUI
import org.lanzadera.proyectos.ui.models.GameUI
import org.lanzadera.proyectos.ui.models.MovieUI
import org.lanzadera.proyectos.ui.models.TvShowUI

/**
 * Temporary in-memory store to pass complex objects between destinations when
 * serializing in the nav route is not desired. 
 * 
 * ⚠️ WARNING: This is a TEMPORARY solution with known issues:
 * - Global mutable state (not thread-safe)
 * - Potential memory leaks (objects never cleared)
 * - Tight coupling between navigation and data
 * - Difficult to test
 * 
 * ✅ Uses UI models to respect Clean Architecture - navigation is part of UI layer.
 * 
 * 🔄 TODO (Phase 1, Task 1.16): Refactor to type-safe navigation with IDs
 * - Pass only IDs in navigation routes (e.g., movieId: Int)
 * - Load data in destination ViewModels using use cases
 * - Remove this NavigationStore entirely
 * 
 * @see https://developer.android.com/guide/navigation/design/type-safety
 */
object NavigationStore {
    var selectedMovie: MovieUI? = null
    var selectedBook: BookUI? = null
    var selectedTvShow: TvShowUI? = null
    var selectedGame: GameUI? = null
}
