package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository

actual fun createFavoriteDetailsRepository(): FavoriteDetailsRepository {
    return object : FavoriteDetailsRepository {
        override fun observeFavoriteTvShows(): Flow<List<TvShow>> = flowOf(emptyList())
        override suspend fun saveFavoriteTvShow(tvShow: TvShow) {}
        override suspend fun removeFavoriteTvShow(tvShowId: String) {}
        override suspend fun getFavoriteTvShow(tvShowId: String): TvShow? = null
        override fun observeFavoriteMovies(): Flow<List<Movie>> = flowOf(emptyList())
        override fun observeUpcomingFavoriteMovies(today: String): Flow<List<Movie>> = flowOf(emptyList())
        override suspend fun saveFavoriteMovie(movie: Movie) {}
        override suspend fun removeFavoriteMovie(movieId: String) {}
        override suspend fun getFavoriteMovie(movieId: String): Movie? = null
    }
}
