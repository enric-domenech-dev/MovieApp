package org.lanzadera.proyectos.ui.components.navComponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import org.lanzadera.proyectos.utils.BottomNavItem
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarIcons
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarSelectedIcons
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarTitles

/**
 * Reusable bottom bar composable that delegates item clicks to the caller using BottomNavItem.
 *
 * Contract:
 * - `selectedItem`: the currently selected BottomNavItem for visual selection; pass null if none.
 * - `onItemSelected`: called with the tapped BottomNavItem. The caller (usually the Navigation host)
 *   should handle navigation (for example using `NavHostController.navigate(route)` with appropriate
 *   options like `launchSingleTop` and `restoreState`). Keeping navigation at the host keeps
 *   responsibilities separated and allows the bottom bar to be pure UI.
 *
 * Example:
 * AppBottomBar(selectedItem = selectedItem) { item ->
 *     navController.navigate(item.route) { ... }
 * }
 */
@Composable
fun AppBottomBar(
    selectedItem: BottomNavItem?,
    modifier: Modifier = Modifier,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NiaNavigationBar(modifier = modifier) {
        BottomNavItem.items.forEach { item ->
            val idx = item.index
            val title = bottomBarTitles.getOrNull(idx) ?: ""
            NiaNavigationBarItem(
                modifier = Modifier.weight(1f),
                icon = { Icon(imageVector = bottomBarIcons.getOrNull(idx)!!, contentDescription = title) },
                selectedIcon = { Icon(imageVector = bottomBarSelectedIcons.getOrNull(idx)!!, contentDescription = title) },
                label = { Text(title) },
                selected = selectedItem?.index == idx,
                onClick = { onItemSelected(item) }
            )
        }
    }
}
