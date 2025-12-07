package org.lanzadera.proyectos.data.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.data.datasource.WatchedEpisodesDataSource
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository

class WatchedEpisodesRepositoryImpl(
    private val dataSource: WatchedEpisodesDataSource
) : WatchedEpisodesRepository {

    override fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>> {
        return dataSource.observeWatchedEpisodes(tvShowId)
    }

    override fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>> {
        return dataSource.observeAllWatchedEpisodes()
    }

    override suspend fun toggleEpisodeWatched(episode: WatchedEpisode, isWatched: Boolean) {
        if (isWatched) {
            dataSource.markAsWatched(episode)
        } else {
            dataSource.markAsUnwatched(episode)
        }
    }

    override suspend fun deleteAllForTvShow(tvShowId: String) {
        dataSource.deleteAllForTvShow(tvShowId)
    }
}
