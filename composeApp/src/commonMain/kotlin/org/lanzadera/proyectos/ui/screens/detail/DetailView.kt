package org.lanzadera.proyectos.ui.screens.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.CustomTopAppBar
import org.lanzadera.proyectos.ui.components.MovieExtendedItem

@Composable
@Preview
fun DetailView(
    nav: NavigationController, movie: Movie,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {
    Scaffold(
        floatingActionButtonPosition =
            FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle click */ },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Movie"
                    )
                }
            )
        },
        topBar = {
            CustomTopAppBar(
                title = movie.title ?: "Movie Details",
                navigationIcon = {
                    IconButton(onClick = { nav.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { nav.navigateBack() }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Movie"
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        },
        content = { paddingValues: PaddingValues ->
            MovieExtendedItem(movie = movie)
        }
    )



//    movie.title?.let {
//        DrawerAppBar(
//            navViewModel = nav,
//            pageTitle = "",
//            actionButton = { },
//            screenContent = {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    MovieExtendedItem(movie = movie)
//                }
//            }
//        )
//    }
}
