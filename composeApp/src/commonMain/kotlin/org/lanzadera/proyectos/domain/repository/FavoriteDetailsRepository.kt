package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

interface FavoriteDetailsRepository {
    // TV Shows
    fun observeFavoriteTvShows(): Flow<List<TvShow>>
    suspend fun saveFavoriteTvShow(tvShow: TvShow)
    suspend fun removeFavoriteTvShow(tvShowId: String)
    suspend fun getFavoriteTvShow(tvShowId: String): TvShow?

    // Movies
    fun observeFavoriteMovies(): Flow<List<Movie>>
    fun observeUpcomingFavoriteMovies(today: String): Flow<List<Movie>>
    suspend fun saveFavoriteMovie(movie: Movie)
    suspend fun removeFavoriteMovie(movieId: String)
    suspend fun getFavoriteMovie(movieId: String): Movie?
}
