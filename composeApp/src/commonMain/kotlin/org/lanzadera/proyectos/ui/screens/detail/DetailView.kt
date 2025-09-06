package org.lanzadera.proyectos.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import movieapp.composeapp.generated.resources.Res
import movieapp.composeapp.generated.resources.navegacion
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.AppTheme
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.CustomTopAppBar
import org.lanzadera.proyectos.ui.components.MovieDetail

@Composable
@Preview
fun DetailView(
    nav: NavigationController, movie: Movie,
    selectedTheme: AppTheme = AppTheme.SYSTEM,
    darkTheme: Boolean = false
) {

    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var isWatched by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.Black,
        modifier = Modifier.safeDrawingPadding(),
        floatingActionButtonPosition =
            FabPosition.EndOverlay,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isFavorite = !isFavorite
                },
                content = {
                    Icon(
                       imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite Button"
                    )
                }
            )
        },
        topBar = {
            CustomTopAppBar(
                title = "",
                navigationIcon = {
                    IconButton(onClick = { nav.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { nav.navigateBack() },
                    ) {
                        Icon(
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            painter = painterResource(Res.drawable.navegacion),
                            contentDescription = "Menu Button"
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        },
        content = { paddingValue ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValue),
            ) {
                MovieDetail(movie = movie)
            }
        }
    )
}
