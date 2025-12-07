package org.lanzadera.proyectos.domain.usecase.episodes

import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository

class ToggleEpisodeWatchedUseCase(
    private val repository: WatchedEpisodesRepository
) {
    suspend operator fun invoke(episode: WatchedEpisode, isWatched: Boolean) {
        repository.toggleEpisodeWatched(episode, isWatched)
    }
}
