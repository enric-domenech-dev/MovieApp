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
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.ui.components.MovieHeader
import org.lanzadera.proyectos.ui.components.TvShowHeader

@Composable
fun FavoriteItemsGrid(
    items: List<FavoriteItem>,
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
            key = { _, item -> "${item.type}_${item.id}" }
        ) { _, item ->
            when (item.type) {
                FavoriteType.MOVIE -> {
                    val movie = Movie(
                        id = item.id.toIntOrNull(),
                        title = item.title,
                        posterPath = item.posterUrl?.substringAfter("w500"),
                        overview = item.overview,
                        releaseDate = null,
                        backdropPath = null
                    )
                    MovieHeader(
                        nav = nav,
                        movie = movie
                    )
                }

                FavoriteType.TV_SHOW -> {
                    val tvShow = TvShow(
                        id = item.id.toIntOrNull(),
                        name = item.title,
                        posterPath = item.posterUrl?.substringAfter("w500"),
                        overview = item.overview,
                        firstAirDate = null,
                        backdropPath = null
                    )
                    TvShowHeader(
                        nav = nav,
                        tvShow = tvShow
                    )
                }

                else -> {
                    // BOOK, GAME - not yet implemented
                }
            }
        }
    }
}
