package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.lanzadera.proyectos.domain.models.WatchedEpisode

class InMemoryWatchedEpisodesDataSource : WatchedEpisodesDataSource {
    private val watchedEpisodes = MutableStateFlow<List<WatchedEpisode>>(emptyList())

    override fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>> {
        return watchedEpisodes.map { episodes ->
            episodes.filter { it.tvShowId == tvShowId }
        }
    }

    override fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>> {
        return watchedEpisodes
    }

    override suspend fun getWatchedEpisodes(tvShowId: String): List<WatchedEpisode> {
        return watchedEpisodes.value.filter { it.tvShowId == tvShowId }
    }

    override suspend fun markAsWatched(episode: WatchedEpisode) {
        val current = watchedEpisodes.value.toMutableList()
        current.removeIf { it.id == episode.id }
        current.add(episode)
        watchedEpisodes.value = current
    }

    override suspend fun markAsUnwatched(episode: WatchedEpisode) {
        watchedEpisodes.value = watchedEpisodes.value.filter { it.id != episode.id }
    }

    override suspend fun deleteAllForTvShow(tvShowId: String) {
        watchedEpisodes.value = watchedEpisodes.value.filter { it.tvShowId != tvShowId }
    }
}
