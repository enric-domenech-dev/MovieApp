package org.lanzadera.proyectos.utils

import org.lanzadera.proyectos.navigation.Screen

/**
 * Enum representing bottom navigation items to avoid magic indices across the codebase.
 * Each entry carries an index and can be matched against Screen routes.
 *
 * Contract / Usage:
 * - Use `BottomNavItem.items` to iterate in the UI.
 * - Use `BottomNavItem.fromRoute(route)` to map the current NavHost route to an item.
 */
enum class BottomNavItem(val index: Int) {
    MENU(0),
    SEARCH(1),
    HOME(2),
    CHAT(3),
    PROFILE(4);

    companion object {
        fun fromRoute(route: String?): BottomNavItem? {
            return when {
                route?.contains("Home") == true -> HOME
                route?.contains("Search") == true -> SEARCH
                route?.contains("Chat") == true -> CHAT
                route?.contains("Profile") == true -> PROFILE
                route?.contains("Settings") == true -> MENU
                else -> null
            }
        }
        
        fun fromIndex(index: Int): BottomNavItem? = entries.firstOrNull { it.index == index }
        val items: List<BottomNavItem> = entries.toList()
    }
}
