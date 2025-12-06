package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository
import org.lanzadera.proyectos.domain.repository.FavoritesRepository
import org.lanzadera.proyectos.domain.repository.MovieRepository
import org.lanzadera.proyectos.domain.repository.TvShowRepository
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository
import org.lanzadera.proyectos.utils.Logger

class ToggleFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository,
    private val favoriteDetailsRepository: FavoriteDetailsRepository,
    private val watchedEpisodesRepository: WatchedEpisodesRepository,
    private val tvShowRepository: TvShowRepository,
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(item: FavoriteItem): Result<Unit> {
        return try {
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
                            Logger.d("Guardando serie $tvShowId con detalles completos", tag = "ToggleFavoriteUseCase")
                            val details = tvShowRepository.getTvShowDetails(tvShowId)
                            if (details != null) {
                                favoriteDetailsRepository.saveFavoriteTvShow(details)
                                Logger.d("Serie guardada - ${details.name}, episodios: ${details.numberOfEpisodes}", tag = "ToggleFavoriteUseCase")
                            } else {
                                Logger.w("No se pudieron obtener detalles de la serie $tvShowId", tag = "ToggleFavoriteUseCase")
                            }
                        }
                    } else {
                        // Eliminar de Room y episodios vistos
                        Logger.d("Eliminando serie ${item.id} de favoritos", tag = "ToggleFavoriteUseCase")
                        favoriteDetailsRepository.removeFavoriteTvShow(item.id)
                        watchedEpisodesRepository.deleteAllForTvShow(item.id)
                    }
                }

                FavoriteType.MOVIE -> {
                    if (isFavorite) {
                        // Obtener detalles completos y guardar en Room
                        val movieId = item.id.toIntOrNull()
                        if (movieId != null) {
                            Logger.d("Guardando película $movieId con detalles completos", tag = "ToggleFavoriteUseCase")
                            val details = movieRepository.getMovieDetails(movieId)
                            if (details != null) {
                                favoriteDetailsRepository.saveFavoriteMovie(details)
                                Logger.d("Película guardada - ${details.title}, fecha: ${details.releaseDate}", tag = "ToggleFavoriteUseCase")
                            } else {
                                Logger.w("No se pudieron obtener detalles de la película $movieId", tag = "ToggleFavoriteUseCase")
                            }
                        }
                    } else {
                        Logger.d("Eliminando película ${item.id} de favoritos", tag = "ToggleFavoriteUseCase")
                        favoriteDetailsRepository.removeFavoriteMovie(item.id)
                    }
                }

                else -> {
                    // Libros y juegos se mantienen solo en el sistema simple
                    Logger.d("Toggle favorito para tipo ${item.type}: ${item.title}", tag = "ToggleFavoriteUseCase")
                }
            }
            
            Result.Success(Unit)
        } catch (e: CancellationException) {
            // Don't catch cancellation - let it propagate
            throw e
        } catch (e: Exception) {
            Logger.e("Error toggling favorite for ${item.title}", tag = "ToggleFavoriteUseCase", throwable = e)
            Result.Error(e, "No se pudo cambiar el estado de favorito")
        }
    }
}

