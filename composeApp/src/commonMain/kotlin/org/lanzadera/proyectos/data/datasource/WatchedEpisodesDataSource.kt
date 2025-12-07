package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedEpisode

interface WatchedEpisodesDataSource {
    fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>>
    fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>>
    suspend fun getWatchedEpisodes(tvShowId: String): List<WatchedEpisode>
    suspend fun markAsWatched(episode: WatchedEpisode)
    suspend fun markAsUnwatched(episode: WatchedEpisode)
    suspend fun deleteAllForTvShow(tvShowId: String)
}
