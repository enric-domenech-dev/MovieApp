package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import org.lanzadera.proyectos.ui.components.MovieHeader
import org.lanzadera.proyectos.ui.components.MovieItem
import org.lanzadera.proyectos.ui.components.navComponents.NiaNavigationBar
import org.lanzadera.proyectos.ui.components.navComponents.NiaNavigationBarItem
import org.lanzadera.proyectos.ui.components.tabs.NiaTab
import org.lanzadera.proyectos.ui.components.tabs.NiaTabRow
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarIcons
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarSelectedIcons
import org.lanzadera.proyectos.utils.Constants.MenuOptions.bottomBarTitles
import org.lanzadera.proyectos.utils.Constants.MenuOptions.topBarTitles

@Composable
@Preview
fun HomeView(
    navIndexBottomBar: Int = 2,
    nav: NavigationController,
    vm: HomeViewModel,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedTab by vm.selectedTab.collectAsStateWithLifecycle()
    val state by vm.uiState.collectAsStateWithLifecycle()
    val primary by vm.primary.collectAsStateWithLifecycle()
    val secondary by vm.secondary.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyGridState()

    // orden derivado para la cabecera (si te conviene)
    val primarySorted by remember(primary) {
        derivedStateOf {
            primary.sortedWith(
                compareByDescending<Movie> { it.releaseDate }
                    .thenByDescending { it.voteCount }
            ).take(40)
        }
    }

    DrawerAppBar(
        modifier = Modifier.safeDrawingPadding(),
        navViewModel = nav,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                val selectedIndex = when (selectedTab) {
                    HomeViewModel.HomeTab.TENDENCIAS -> 0
                    HomeViewModel.HomeTab.PELICULAS -> 1
                    HomeViewModel.HomeTab.SERIES -> 2
                    HomeViewModel.HomeTab.FAVORITOS -> 3
                }
                NiaTabRow(selectedTabIndex = selectedIndex) {
                    topBarTitles.forEachIndexed { index, title ->
                        NiaTab(
                            selected = selectedIndex == index,
                            onClick = { vm.selectTab(index) },   // <- el VM manda
                            text = {
                                Text(
                                    text = title.uppercase(),
                                    style = TextStyle(
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        fontFamily = FontFamily.SansSerif,
                                        color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                )
                            },
                        )
                    }
                }
            },
            bottomBar = {
                var selectedItem by rememberSaveable { mutableIntStateOf(navIndexBottomBar) }
                NiaNavigationBar {
                    bottomBarTitles.forEachIndexed { index, item ->
                        NiaNavigationBarItem(
                            modifier = Modifier.weight(1f),
                            icon = {
                                Icon(
                                    imageVector = bottomBarIcons[index],
                                    contentDescription = item
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    imageVector = bottomBarSelectedIcons[index],
                                    contentDescription = item
                                )
                            },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                when (index) {
                                    0 -> scope.launch {
                                        if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                    }

                                    1 -> nav.navigateToSearch()
                                    2 -> Unit
                                    3 -> nav.navigateToSearch()
                                    4 -> nav.navigateToSearch()
                                }
                            },
                        )
                    }
                }
            }
        ) { paddingValues ->

//            Text(
//                "\nisLoading: ${state.isLoading}, \nerror: ${state.error}, " +
//                        "\nprimary: ${primary.size}, \nsecondary: ${secondary.size} ",
//                modifier = Modifier.padding(paddingValues)
//            )

            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val hasContent = primarySorted.isNotEmpty() || secondary.isNotEmpty()
                val showLoading = state.isLoading && !hasContent
                val showError = (state.error != null) && !hasContent

                when {
                    showLoading -> {
                        CircularProgressIndicator()
                    }

                    showError -> {
                        Text(text = "Error", color = MaterialTheme.colorScheme.error)
                        Text(text = state.error ?: "", color = MaterialTheme.colorScheme.error)
                        // Opcional: botón de reintento
                        Button(onClick = { vm.refresh(force = true) }) { Text("Reintentar") }
                    }

                    else -> {

                        if (!hasContent) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Filled.Info,
                                        contentDescription = "Información",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = 12.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "No hay contenido",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Text(
                                            text = "Intenta actualizar o cambia de pestaña.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Surface(
                                        shape = androidx.compose.foundation.shape.CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.padding(top = 12.dp)
                                    ) {
                                        androidx.compose.material3.IconButton(onClick = {
                                            vm.refresh(
                                                force = true
                                            )
                                        }) {
                                            Icon(
                                                imageVector = androidx.compose.material.icons.Icons.Filled.Refresh,
                                                contentDescription = "Reintentar",
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                columns = GridCells.Adaptive(minSize = 120.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Titles depending on the selected tab
                                val (primaryTitle, secondaryTitle) = when (selectedTab) {
                                    HomeViewModel.HomeTab.TENDENCIAS -> "Tendencias" to "En cartelera"
                                    HomeViewModel.HomeTab.PELICULAS -> "Populares" to "Mejor valoradas"
                                    HomeViewModel.HomeTab.SERIES -> "Tendencias (semana)" to "Próximamente"
                                    HomeViewModel.HomeTab.FAVORITOS -> "Favoritos" to "Descubrir"
                                }

                                // Tendencias / primary (si hay)
                                if (primarySorted.isNotEmpty()) {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Text(
                                            text = primaryTitle,
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            itemsIndexed(
                                                items = primarySorted,
                                                key = { index, m -> "${m.id}_$index" }
                                            ) { _, movie ->
                                                MovieHeader(nav, movie)
                                            }
                                        }
                                    }
                                }

                                // Películas / secondary (si hay)
                                if (secondary.isNotEmpty()) {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Text(
                                            text = secondaryTitle,
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                    }
                                    itemsIndexed(
                                        items = secondary,
                                        key = { index, movie -> "${movie.id}_$index" }
                                    ) { _, movie ->
                                        MovieItem(nav, movie)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
