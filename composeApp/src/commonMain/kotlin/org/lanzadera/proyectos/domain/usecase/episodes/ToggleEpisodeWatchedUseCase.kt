package org.lanzadera.proyectos.domain.usecase.episodes

import kotlinx.coroutines.CancellationException
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository
import org.lanzadera.proyectos.utils.Logger

class ToggleEpisodeWatchedUseCase(
    private val repository: WatchedEpisodesRepository
) {
    suspend operator fun invoke(episode: WatchedEpisode, isWatched: Boolean): Result<Unit> {
        return try {
            repository.toggleEpisodeWatched(episode, isWatched)
            Logger.d("Episode toggled: ${episode.id}, watched=$isWatched", tag = "ToggleEpisodeWatchedUseCase")
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error toggling episode watched status", tag = "ToggleEpisodeWatchedUseCase", throwable = e)
            Result.Error(e, "Error al marcar el episodio")
        }
    }
}
