package org.lanzadera.proyectos.utils

/**
 * Enum representing bottom navigation items to avoid magic indices across the codebase.
 * Each entry carries an index and the navigation route (from `Constants.Screen`).
 *
 * Contract / Usage:
 * - Use `BottomNavItem.items` to iterate in the UI.
 * - Use `BottomNavItem.fromRoute(route)` to map the current NavHost route to an item.
 * - The `route` value is the same route used by your NavHost composable destinations.
 */
enum class BottomNavItem(val index: Int, val route: String) {
    MENU(0, Constants.Screen.Settings.route),
    SEARCH(1, Constants.Screen.Search.route),
    HOME(2, Constants.Screen.Home.route),
    CHAT(3, Constants.Screen.Chat.route),
    PROFILE(4, Constants.Screen.Profile.route);

    companion object {
        fun fromRoute(route: String?): BottomNavItem? = entries.firstOrNull { it.route == route }
        fun fromIndex(index: Int): BottomNavItem? = entries.firstOrNull { it.index == index }
        val items: List<BottomNavItem> = entries.toList()
    }
}
