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
import androidx.compose.material.icons.filled.Favorite
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
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.ui.components.CustomTopAppBar
import org.lanzadera.proyectos.ui.components.SeriesCreditsTab
import org.lanzadera.proyectos.ui.components.SeriesInfoTab
import org.lanzadera.proyectos.ui.components.SeriesSeasonsTab
import org.lanzadera.proyectos.ui.models.TvShowUI
import org.lanzadera.proyectos.utils.Strings

@Composable
@Preview
fun SeriesDetailView(
    onNavigateBack: () -> Unit,
    tvShow: TvShowUI? = null,
    tvShowId: Int? = null,
    vm: SeriesDetailViewModel = koinInject()
) {
    val tvShowDetail by vm.tvShowDetail.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val favorites by vm.favorites.collectAsStateWithLifecycle()
    val watchedEpisodes by vm.watchedEpisodes.collectAsStateWithLifecycle()
    val selectedTab = remember { mutableStateOf(0) }
    val isFavorite = remember(tvShowDetail, favorites) {
        favorites.any { it.id == tvShowDetail?.id?.toString() && it.type == FavoriteType.TV_SHOW }
    }
    val watchedEpisodesSet = remember(watchedEpisodes) {
        watchedEpisodes.map { "${it.seasonNumber}-${it.episodeNumber}" }.toSet()
    }

    // Load by ID from parameter
    LaunchedEffect(tvShow, tvShowId) {
        val idToLoad = tvShowId ?: tvShow?.id
        idToLoad?.let {
            vm.loadTvShowDetails(it)
        }
    }

    val displayedTvShow = tvShowDetail

    if (displayedTvShow == null) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            topBar = {
                CustomTopAppBar(
                    title = Strings.Detail.NO_DATA,
                    navigationIcon = {
                        IconButton(onClick = { onNavigateBack() }) {
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
                Text(Strings.Detail.NO_DATA, color = MaterialTheme.colorScheme.error)
            }
        }
        return
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        floatingActionButtonPosition = FabPosition.EndOverlay,
        topBar = {
            CustomTopAppBar(
                title = displayedTvShow.name ?: "",
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
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
                        // Like Button - Deshabilitado mientras carga
                        IconButton(
                            onClick = { vm.toggleFavorite() },
                            modifier = Modifier.size(40.dp),
                            enabled = !isLoading
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Agregar a favoritos",
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = if (isLoading) {
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f)
                                } else if (isFavorite) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onBackground
                                }
                            )
                        }
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
                            .padding(paddingValue)
                    ) {
                        // Backdrop - Always visible, with swipe detection only on image
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectHorizontalDragGestures { change, dragAmount ->
                                        change.consume()
                                        when {
                                            dragAmount > 100 && selectedTab.value > 0 -> selectedTab.value--
                                            dragAmount < -100 && selectedTab.value < 2 -> selectedTab.value++
                                        }
                                    }
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/original${displayedTvShow.backdropPath}",
                                contentDescription = displayedTvShow.name,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                            )
                        }

                        // Tab Row with better spacing
                        TabRow(
                            selectedTabIndex = selectedTab.value,
                            modifier = Modifier
                                .fillMaxWidth(),
                            divider = {}
                        ) {
                            Tab(
                                selected = selectedTab.value == 0,
                                onClick = { selectedTab.value = 0 },
                                icon = { Icon(Icons.Outlined.Info, contentDescription = "Info") }
                            )
                            Tab(
                                selected = selectedTab.value == 1,
                                onClick = { selectedTab.value = 1 },
                                text = {
                                    Text(
                                        "Temporadas",
                                        maxLines = 1
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab.value == 2,
                                onClick = { selectedTab.value = 2 },
                                text = {
                                    Text(
                                        "Créditos",
                                        maxLines = 1
                                    )
                                }
                            )
                        }

                        // Tab Content - scrollable with proper padding
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (selectedTab.value) {
                                0 -> SeriesInfoTab(tvShow = displayedTvShow)
                                1 -> {
                                    if (isLoading) {
                                        // Mostrar loading en el tab de temporadas
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                CircularProgressIndicator()
                                                Text(
                                                    "Cargando temporadas y episodios...",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    } else {
                                        SeriesSeasonsTab(
                                            tvShow = displayedTvShow,
                                            watchedEpisodes = watchedEpisodesSet,
                                            onEpisodeToggle = { season, episode, isWatched ->
                                                vm.toggleEpisodeWatched(season, episode, isWatched)
                                            }
                                        )
                                    }
                                }
                                2 -> SeriesCreditsTab(tvShow = displayedTvShow)
                            }
                        }
                    }
                }
            }
        }
    )
}
