package org.lanzadera.proyectos.ui.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lanzadera.proyectos.ui.components.MovieHeader
import org.lanzadera.proyectos.ui.components.MovieHeaderWithReleaseInfo
import org.lanzadera.proyectos.ui.components.TvShowHeaderFinished
import org.lanzadera.proyectos.ui.components.TvShowHeaderWithNextEpisode
import org.lanzadera.proyectos.ui.models.FavoriteItemWithInfoUI

@Composable
fun FavoriteItemsGrid(
    items: List<FavoriteItemWithInfoUI>,
    showMovies: Boolean,
    showSeries: Boolean,
    onMoviesFilterToggle: () -> Unit,
    onSeriesFilterToggle: () -> Unit,
    onMovieClick: (movieId: Int) -> Unit,
    onTvShowClick: (tvShowId: Int) -> Unit,
    hasNoFavorites: Boolean,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 16.dp,
            bottom = 80.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Filter chips as first item (spans both columns) - ALWAYS VISIBLE
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = showSeries,
                    onClick = onSeriesFilterToggle,
                    label = { Text("Series") },
                    leadingIcon = if (showSeries) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    } else null
                )
                
                FilterChip(
                    selected = showMovies,
                    onClick = onMoviesFilterToggle,
                    label = { Text("Películas") },
                    leadingIcon = if (showMovies) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    } else null
                )
            }
        }
        
        // Empty state message when no favorites exist
        if (hasNoFavorites) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No hay contenido por seguir",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        // Empty state message when filters don't match any items
        else if (items.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No hay contenido con los filtros seleccionados",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        itemsIndexed(
            items = items,
            key = { _, item -> item.id }
        ) { _, item ->
            when (item) {
                is FavoriteItemWithInfoUI.MovieItem -> {
                    MovieHeaderWithReleaseInfo(
                        movieWithRelease = item.movieWithRelease,
                        onMovieClick = onMovieClick
                    )
                }

                is FavoriteItemWithInfoUI.TvShowItem -> {
                    TvShowHeaderWithNextEpisode(
                        tvShowWithNext = item.tvShowWithNext,
                        onTvShowClick = onTvShowClick
                    )
                }

                is FavoriteItemWithInfoUI.WatchedMovieItem -> {
                    MovieHeader(
                        movie = item.movie,
                        onMovieClick = onMovieClick
                    )
                }

                is FavoriteItemWithInfoUI.FinishedSeriesItem -> {
                    TvShowHeaderFinished(
                        tvShow = item.tvShow,
                        onTvShowClick = onTvShowClick
                    )
                }
            }
        }
    }
}

