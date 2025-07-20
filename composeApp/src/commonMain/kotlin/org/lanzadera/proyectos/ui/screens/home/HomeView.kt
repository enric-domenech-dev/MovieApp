package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import org.lanzadera.proyectos.ui.components.MovieItem

@Composable
@Preview
fun HomeView(
    nav: NavigationController, vm: HomeViewModel
) {
    var movies by remember { mutableStateOf<List<Movie>?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        scope.launch {
            try {
                delay(5000)
                movies = vm.fetchAllMovies()
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

    DrawerAppBar(
        navViewModel = nav,
        pageTitle = "HOME",
        actionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Try open camera here
                },
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        screenContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {

                    itemsIndexed(movies ?: emptyList()) { index, movie ->
                        MovieItem(nav, movie)
                    }
                }
            }
        }
    )
}


