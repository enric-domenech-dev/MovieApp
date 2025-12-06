package org.lanzadera.proyectos.ui.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.lanzadera.proyectos.ui.models.FavoriteItemWithInfoUI
import org.lanzadera.proyectos.ui.components.MovieHeader
import org.lanzadera.proyectos.ui.components.MovieHeaderWithReleaseInfo
import org.lanzadera.proyectos.ui.components.TvShowHeaderFinished
import org.lanzadera.proyectos.ui.components.TvShowHeaderWithNextEpisode

@Composable
fun FavoriteItemsGrid(
    items: List<FavoriteItemWithInfoUI>,
    nav: NavHostController,
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
        itemsIndexed(
            items = items,
            key = { _, item -> item.id }
        ) { _, item ->
            when (item) {
                is FavoriteItemWithInfoUI.MovieItem -> {
                    MovieHeaderWithReleaseInfo(
                        nav = nav,
                        movieWithRelease = item.movieWithRelease
                    )
                }

                is FavoriteItemWithInfoUI.TvShowItem -> {
                    TvShowHeaderWithNextEpisode(
                        nav = nav,
                        tvShowWithNext = item.tvShowWithNext
                    )
                }

                is FavoriteItemWithInfoUI.WatchedMovieItem -> {
                    MovieHeader(
                        nav = nav,
                        movie = item.movie
                    )
                }

                is FavoriteItemWithInfoUI.FinishedSeriesItem -> {
                    TvShowHeaderFinished(
                        nav = nav,
                        tvShow = item.tvShow
                    )
                }
            }
        }
    }
}

