package org.lanzadera.proyectos.ui.screens.search

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.Navigation.NiaNavigationBar
import org.lanzadera.proyectos.ui.components.Navigation.NiaNavigationBarItem
import org.lanzadera.proyectos.ui.components.tabs.NiaTab
import org.lanzadera.proyectos.ui.components.tabs.NiaTabRow

@Composable
@Preview
fun SearchView(
    navIndexBottomBar: Int = 1,
    nav: NavigationController, vm: SearchViewModel,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            val titles = listOf("Tendencias", "Películas", "Series", "Favoritos")
            NiaTabRow(selectedTabIndex = selectedTabIndex) {
                titles.forEachIndexed { index, title ->
                    NiaTab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) },
                    )
                }
            }
        },
        bottomBar = {

            var selectedItem by rememberSaveable { mutableIntStateOf(navIndexBottomBar) }
            val items = listOf("Menu", "Buscar", "Inicio", "Chat", "Perfil")
            val icons = listOf(
                Icons.AutoMirrored.Outlined.List,
                Icons.Outlined.Search,
                Icons.Outlined.Home,
                Icons.Outlined.MailOutline,
                Icons.Outlined.Person
            )
            val selectedIcons = listOf(
                Icons.AutoMirrored.Filled.List,
                Icons.Filled.Search,
                Icons.Filled.Home,
                Icons.Filled.MailOutline,
                Icons.Filled.Person
            )
            NiaNavigationBar {
                items.forEachIndexed { index, item ->
                    NiaNavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = item,
                            )
                        },
                        selectedIcon = {
                            Icon(
                                imageVector = selectedIcons[index],
                                contentDescription = item,
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            when (index) {
                                0 -> {
                                    nav.navigateToSearch()
                                }

                                1 -> {
                                    nav.navigateToSearch()
                                }

                                2 -> {
                                    nav.navigateToHome()
                                }

                                3 -> {
                                    nav.navigateToSearch()
                                }

                                4 -> {
                                    nav.navigateToSearch()
                                }
                            }
                        },
                    )
                }
            }
        }
    ) { paddingValues ->

    }
}