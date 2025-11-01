package org.lanzadera.proyectos.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.navigation.NavigationStore
import org.lanzadera.proyectos.ui.components.CustomTopAppBar
import org.lanzadera.proyectos.ui.components.TvShowDetail

@Composable
@Preview
fun SeriesDetailView(
    nav: NavHostController,
    tvShow: TvShow?,
    vm: SeriesDetailViewModel = koinInject()
) {
    val tvShowDetail by vm.tvShowDetail.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()

    // Si recibimos un tvShow del NavigationStore, úsalo
    // Si no, intenta cargar por ID (para casos donde se recarga la página)
    LaunchedEffect(tvShow) {
        when {
            tvShow != null -> vm.setTvShowDetail(tvShow)
            NavigationStore.selectedTvShow != null -> vm.setTvShowDetail(NavigationStore.selectedTvShow!!)
            else -> nav.popBackStack() // Fallback: navega atrás si no hay datos
        }
    }

    val displayedTvShow = tvShowDetail ?: tvShow ?: NavigationStore.selectedTvShow

    if (displayedTvShow == null) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            topBar = {
                CustomTopAppBar(
                    title = "Serie",
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Go Back",
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
                Text("No se encontraron datos de la serie", color = MaterialTheme.colorScheme.error)
            }
        }
        return
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            // BUTTON
        },
        topBar = {
            CustomTopAppBar(
                title = displayedTvShow.name ?: "",
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Go Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        },
        content = { paddingValue ->
            when {
                isLoading && tvShowDetail == null -> {
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

                error != null && tvShowDetail == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValue),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error ?: "Error desconocido", color = MaterialTheme.colorScheme.error)
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValue),
                    ) {
                        TvShowDetail(tvShow = displayedTvShow)
                    }
                }
            }
        }
    )
}

