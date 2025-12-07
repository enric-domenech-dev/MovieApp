package org.lanzadera.proyectos.domain.usecase.movies

import kotlinx.coroutines.CancellationException
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository
import org.lanzadera.proyectos.utils.Logger

class ToggleMovieWatchedUseCase(
    private val repository: WatchedMoviesRepository
) {
    suspend operator fun invoke(movieId: String, isWatched: Boolean): Result<Unit> {
        return try {
            repository.toggleMovieWatched(movieId, isWatched)
            Logger.d("Toggled watched status for movie: $movieId to $isWatched", tag = "ToggleMovieWatchedUseCase")
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error toggling watched status for movie: $movieId", tag = "ToggleMovieWatchedUseCase", throwable = e)
            Result.Error(e, "No se pudo cambiar el estado de vista")
        }
    }
}
