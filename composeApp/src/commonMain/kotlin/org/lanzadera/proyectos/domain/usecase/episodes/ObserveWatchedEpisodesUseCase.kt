package org.lanzadera.proyectos.domain.usecase.episodes

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository

class ObserveWatchedEpisodesUseCase(
    private val repository: WatchedEpisodesRepository
) {
    operator fun invoke(tvShowId: String): Flow<List<WatchedEpisode>> {
        return repository.observeWatchedEpisodes(tvShowId)
    }
}
