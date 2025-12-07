package org.lanzadera.proyectos.domain.usecase.episodes

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository

/**
 * Use case to observe all watched episodes across all TV shows.
 * 
 * Returns a Flow that emits the complete list of watched episodes.
 */
class ObserveAllWatchedEpisodesUseCase(
    private val repository: WatchedEpisodesRepository
) {
    operator fun invoke(): Flow<List<WatchedEpisode>> {
        return repository.observeAllWatchedEpisodes()
    }
}
