package org.lanzadera.proyectos.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.DrawerAppBar
import org.lanzadera.proyectos.ui.components.MovieExtendedItem
import org.lanzadera.proyectos.ui.components.MovieItem
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel

@Composable
@Preview
fun DetailView(
    nav: NavigationController, movie: Movie
) {
    movie.title?.let {
        DrawerAppBar(
            navViewModel = nav,
            pageTitle = "",
            actionButton = { },
            screenContent = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MovieExtendedItem(movie = movie)
                }
            }
        )
    }
}
