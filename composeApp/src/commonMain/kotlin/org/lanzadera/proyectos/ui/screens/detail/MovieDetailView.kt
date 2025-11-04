package org.lanzadera.proyectos.ui.screens.detail

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.lanzadera.proyectos.ui.components.CustomTopAppBar
import org.lanzadera.proyectos.ui.components.MovieCreditsTab
import org.lanzadera.proyectos.ui.components.MovieInfoTabContent
import org.lanzadera.proyectos.utils.Strings

@Composable
@Preview
fun MovieDetailView(
    nav: NavHostController,
    movieId: Int,
    viewModel: MovieDetailViewModel = koinInject()
) {
    val movieDetail by viewModel.movieDetail.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    // Cargar los detalles de la película cuando el composable se monta
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    if (movieDetail == null && isLoading) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            topBar = {
                CustomTopAppBar(
                    title = "Cargando...",
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }

    if (movieDetail == null || error != null) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            topBar = {
                CustomTopAppBar(
                    title = Strings.Detail.NO_DATA,
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Volver",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(error ?: Strings.Detail.NO_DATA, color = MaterialTheme.colorScheme.error)
            }
        }
        return
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        floatingActionButtonPosition = FabPosition.EndOverlay,
        topBar = {
            CustomTopAppBar(
                title = movieDetail?.title ?: "",
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Like Button - Green
                        IconButton(
                            onClick = { /* TODO: Implement favorite functionality */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Agregar a favoritos",
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = androidx.compose.ui.graphics.Color(0xFF2AE98E)
                            )
                        }
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        },
        content = { paddingValue ->
            val selectedTab = remember { mutableStateOf(0) }

            when {
                isLoading && movieDetail == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValue),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null && movieDetail == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValue),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error ?: Strings.Detail.NO_DATA, color = MaterialTheme.colorScheme.error)
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValue)
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures { change, dragAmount ->
                                    change.consume()
                                    when {
                                        dragAmount > 50 && selectedTab.value > 0 -> selectedTab.value--
                                        dragAmount < -50 && selectedTab.value < 1 -> selectedTab.value++
                                    }
                                }
                            }
                    ) {
                        // Backdrop - Always visible
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/original${movieDetail?.backdropPath}",
                                contentDescription = movieDetail?.title,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                            )
                        }

                        // Tab Row
                        TabRow(
                            selectedTabIndex = selectedTab.value,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Tab(
                                selected = selectedTab.value == 0,
                                onClick = { selectedTab.value = 0 },
                                icon = { Icon(Icons.Outlined.Info, contentDescription = "Info") }
                            )
                            Tab(
                                selected = selectedTab.value == 1,
                                onClick = { selectedTab.value = 1 },
                                text = { Text("Créditos") }
                            )
                        }

                        // Tab Content
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (selectedTab.value) {
                                0 -> MovieInfoTabContent(movie = movieDetail)
                                1 -> MovieCreditsTab(movie = movieDetail)
                            }
                        }
                    }
                }
            }
        }
    )
}

