package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.flow.first
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository
import org.lanzadera.proyectos.domain.repository.FavoritesRepository
import org.lanzadera.proyectos.domain.repository.MovieRepository
import org.lanzadera.proyectos.domain.repository.TvShowRepository
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository

class ToggleFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository,
    private val favoriteDetailsRepository: FavoriteDetailsRepository,
    private val watchedEpisodesRepository: WatchedEpisodesRepository,
    private val tvShowRepository: TvShowRepository,
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(item: FavoriteItem) {
        // Toggle en el sistema antiguo (favoritos simples)
        favoritesRepository.toggleFavorite(item)

        // Verificar si ahora es favorito
        val isFavorite = favoritesRepository.favorites.first()
            .any { it.id == item.id && it.type == item.type }

        when (item.type) {
            FavoriteType.TV_SHOW -> {
                if (isFavorite) {
                    // Obtener detalles completos y guardar en Room
                    val tvShowId = item.id.toIntOrNull()
                    if (tvShowId != null) {
                        println("ToggleFavorite: Guardando serie $tvShowId con detalles completos")
                        val details = tvShowRepository.getTvShowDetails(tvShowId)
                        if (details != null) {
                            favoriteDetailsRepository.saveFavoriteTvShow(details)
                            println("ToggleFavorite: Serie guardada - ${details.name}, episodios: ${details.numberOfEpisodes}")
                        }
                    }
                } else {
                    // Eliminar de Room y episodios vistos
                    println("ToggleFavorite: Eliminando serie ${item.id} de favoritos")
                    favoriteDetailsRepository.removeFavoriteTvShow(item.id)
                    watchedEpisodesRepository.deleteAllForTvShow(item.id)
                }
            }

            FavoriteType.MOVIE -> {
                if (isFavorite) {
                    // Obtener detalles completos y guardar en Room
                    val movieId = item.id.toIntOrNull()
                    if (movieId != null) {
                        println("ToggleFavorite: Guardando película $movieId con detalles completos")
                        val details = movieRepository.getMovieDetails(movieId)
                        if (details != null) {
                            favoriteDetailsRepository.saveFavoriteMovie(details)
                            println("ToggleFavorite: Película guardada - ${details.title}, fecha: ${details.releaseDate}")
                        }
                    }
                } else {
                    println("ToggleFavorite: Eliminando película ${item.id} de favoritos")
                    favoriteDetailsRepository.removeFavoriteMovie(item.id)
                }
            }

            else -> {
                // Libros y juegos se mantienen solo en el sistema simple
            }
        }
    }
}

