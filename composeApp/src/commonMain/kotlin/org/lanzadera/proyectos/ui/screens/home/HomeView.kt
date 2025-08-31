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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import org.lanzadera.proyectos.ui.components.MovieHeader
import org.lanzadera.proyectos.ui.components.MovieItem
import org.lanzadera.proyectos.ui.components.Navigation.NiaNavigationBar
import org.lanzadera.proyectos.ui.components.Navigation.NiaNavigationBarItem
import org.lanzadera.proyectos.ui.components.tabs.NiaTab
import org.lanzadera.proyectos.ui.components.tabs.NiaTabRow

@Composable
@Preview
fun HomeView(
    navIndexBottomBar: Int = 2,
    nav: NavigationController, vm: HomeViewModel,
//    selectedTheme: AppTheme = AppTheme.SYSTEM,
//    darkTheme: Boolean = false
) {
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    var uiState by remember { mutableStateOf<HomeViewModel.UIState>(HomeViewModel.UIState.Loading) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        scope.launch {
            uiState = vm.initUIState()
        }
    }
    DrawerAppBar(
        navViewModel = nav,
        drawerState = drawerState,
    ){
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
                                        // Do nothing, we are already in home
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
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (uiState) {
                    is HomeViewModel.UIState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is HomeViewModel.UIState.Success -> {

                        if ((uiState as HomeViewModel.UIState.Success).movies.isEmpty()) {
                            Text(text = "No movies found.")
                        } else {

                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 120.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(
                                        text = "Proximamente",
                                        style = MaterialTheme.typography.headlineSmall,
                                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                                    )
                                }

                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        val successState = uiState as HomeViewModel.UIState.Success

                                        val sortedMovies =
                                            successState.trendingMovies.sortedWith(compareByDescending<Movie> {
                                                it.releaseDate
                                            }.thenByDescending { it.voteCount })

                                        itemsIndexed(sortedMovies.take(20)) { index, movie ->
                                            MovieHeader(nav, movie)
                                        }
                                    }
                                }

                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(
                                        text = "Más buscadas",
                                        style = MaterialTheme.typography.headlineSmall,
                                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                    )
                                }

                                items((uiState as HomeViewModel.UIState.Success).movies) { movie ->
                                    MovieItem(nav, movie)
                                }
                            }
                        }
                    }

                    is HomeViewModel.UIState.Error -> {
                        Text(
                            text = (uiState as HomeViewModel.UIState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }


}
