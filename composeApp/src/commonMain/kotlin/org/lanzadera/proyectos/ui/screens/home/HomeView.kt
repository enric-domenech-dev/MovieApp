package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.CustomBottomAppBar
import org.lanzadera.proyectos.ui.components.MovieHeader
import org.lanzadera.proyectos.ui.components.MovieItem
import org.lanzadera.proyectos.ui.components.SingleChoiceSegmentedButtonAlternativeExample

@Composable
@Preview
fun HomeView(
    nav: NavigationController, vm: HomeViewModel,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {
    var uiState by remember { mutableStateOf<HomeViewModel.UIState>(HomeViewModel.UIState.Loading) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        scope.launch {
            uiState = vm.initUIState()
        }
    }

    Scaffold (
        bottomBar = {
            CustomBottomAppBar(
                containerColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                content = {
                    SingleChoiceSegmentedButtonAlternativeExample(
                        onClickNavigate = { selectedIndex ->
                            when (selectedIndex) {
                                0 -> nav.navigateToSearch()
//                                1 -> nav.navigateToFavorites()
                                2 -> nav.navigateToHome()
//                                3 -> nav.navigateToList()
//                                4 -> nav.navigateToProfile()
                            }
                        }
                    )
                }
            )
        }
    ){
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (uiState) {
                is HomeViewModel.UIState.Loading -> {
                    CircularProgressIndicator()
                }

                is HomeViewModel.UIState.Success -> {
                    // Aquí puedes decidir mostrar todas las películas o solo las trending
                    // En este ejemplo muestro todas las películas (movies)
                    // También tienes disponible uiState.trendingMovies si las necesitas

                    if ((uiState as HomeViewModel.UIState.Success).movies.isEmpty()) {
                        Text(text = "No movies found.")
                    } else {

                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 120.dp),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Header
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = "Top 10 más vistas",
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            // Top 10 horizontal (con altura fija)
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    itemsIndexed((uiState as HomeViewModel.UIState.Success).trendingMovies.take(10)) { index, movie ->
                                        Box {
                                            MovieHeader(nav, movie, index)
                                        }
                                    }
                                }
                            }

                            // Título de sección
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = "Más buscadas",
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                                )
                            }

                            // Grid de películas más buscadas
                            items((uiState as HomeViewModel.UIState.Success).movies) { movie ->
                                MovieItem(nav, movie)
                            }
                        }


                    }
                }

                is HomeViewModel.UIState.Error -> {
                    Text(
                        text = (uiState as HomeViewModel.UIState.Error).message,
                        color = MaterialTheme.colors.error
                    )
                }
            }
        }
    }
}
