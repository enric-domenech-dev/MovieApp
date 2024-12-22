package org.lanzadera.proyectos.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lanzadera.proyectos.CameraView
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.navigation.NavigationController
import org.lanzadera.proyectos.ui.components.DrawerAppBar


@Composable
@Preview
fun HomeView(
    nav: NavigationController, vm: HomeViewModel
) {
    var movies by remember { mutableStateOf<List<Movie>?>(null) }
    val number = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("Loading") }
    var isClicked by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        scope.launch {
            try {
                delay(5000)
                movies = vm.data()
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
                    isClicked = true
                    number.value++
                },
                backgroundColor = MaterialTheme.colors.primary,
            ) {

                Icon(Icons.Default.Add, contentDescription = "Add")
                if (isClicked) {
                   // CameraView()
                }
            }
        },
        screenContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("${number.value}")
                Spacer(modifier = Modifier.height(8.dp))


                LazyColumn {
                    items(movies ?: emptyList()) { movie ->

                        var isFilled by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clickable { isFilled = !isFilled },
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(if (!movie.adult) MaterialTheme.colors.error else MaterialTheme.colors.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (!movie.adult) {
                                            Text(
                                                text = "18+",
                                                style = MaterialTheme.typography.body1,
                                                color = MaterialTheme.colors.onPrimary
                                            )
                                        } else {
                                            Icon(
                                                modifier = Modifier.size(25.dp),
                                                imageVector = Icons.Filled.Person,
                                                contentDescription = "Star Icon",
                                                tint = MaterialTheme.colors.onPrimary
                                            )
                                        }

                                    }

                                    Icon(
                                        modifier = Modifier.size(25.dp),
                                        imageVector = if (isFilled) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Star Icon",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = movie.originalTitle,
                                    style = MaterialTheme.typography.h5,
                                )
                                Text(
                                    text = movie.releaseDate,
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.primaryVariant
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = movie.overview,
                                    style = MaterialTheme.typography.body2
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun GreetingView(text: String) {
    Text(text = text)
}
