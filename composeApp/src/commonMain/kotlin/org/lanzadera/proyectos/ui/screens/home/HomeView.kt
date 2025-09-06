package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
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

@Composable
@Preview
fun HomeView(
    navIndexBottomBar: Int = 2,
    nav: NavigationController,
    vm: HomeViewModel,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val movies by vm.movies.collectAsStateWithLifecycle()
    val trendingMovies by vm.trending.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // Orden derivado y memoizado
    val sortedTrending by remember(trendingMovies) {
        derivedStateOf {
            trendingMovies
                .sortedWith(compareByDescending<Movie> { it.releaseDate }
                    .thenByDescending { it.voteCount })
                .take(30)
        }
    }

    DrawerAppBar(
        modifier = Modifier.safeDrawingPadding(),
        navViewModel = nav,
        drawerState = drawerState
    ) {
        Scaffold(
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
                            icon = { Icon(imageVector = icons[index], contentDescription = item) },
                            selectedIcon = { Icon(imageVector = selectedIcons[index], contentDescription = item) },
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
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator()
                    }
                    uiState.error != null -> {
                        Text(text = uiState.error ?: "", color = MaterialTheme.colorScheme.error)
                    }
                    movies.isEmpty() && sortedTrending.isEmpty() -> {
                        Text(text = "No hay contenido para mostrar.")
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 120.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Tendencias (si hay)
                            if (sortedTrending.isNotEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(
                                        text = "Tendencias",
                                        style = MaterialTheme.typography.headlineSmall,
                                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                                    )
                                }
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        itemsIndexed(
                                            items = sortedTrending,
                                            key = { _, m -> m.id ?: m.hashCode() }
                                        ) { _, movie ->
                                            MovieHeader(nav, movie)
                                        }
                                    }
                                }
                            }

                            // Películas (si hay)
                            if (movies.isNotEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(
                                        text = "Más buscadas",
                                        style = MaterialTheme.typography.headlineSmall,
                                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                    )
                                }
                                items(
                                    items = movies,
                                    key = { it.id ?: it.hashCode() }
                                ) { movie ->
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
