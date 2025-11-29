package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedEpisode

interface WatchedEpisodesRepository {
    fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>>
    fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>>
    suspend fun toggleEpisodeWatched(episode: WatchedEpisode, isWatched: Boolean)
    suspend fun deleteAllForTvShow(tvShowId: String)
}
