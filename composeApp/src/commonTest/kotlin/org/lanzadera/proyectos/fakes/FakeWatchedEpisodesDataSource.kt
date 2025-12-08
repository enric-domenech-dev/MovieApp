package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.lanzadera.proyectos.data.datasource.WatchedEpisodesDataSource
import org.lanzadera.proyectos.domain.models.WatchedEpisode

class FakeWatchedEpisodesDataSource : WatchedEpisodesDataSource {
    
    private val _watchedEpisodes = MutableStateFlow<List<WatchedEpisode>>(emptyList())
    
    override fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>> {
        return _watchedEpisodes.map { episodes ->
            episodes.filter { it.tvShowId == tvShowId }
        }
    }
    
    override fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>> {
        return _watchedEpisodes.asStateFlow()
    }
    
    override suspend fun getWatchedEpisodes(tvShowId: String): List<WatchedEpisode> {
        return _watchedEpisodes.value.filter { it.tvShowId == tvShowId }
    }
    
    override suspend fun markAsWatched(episode: WatchedEpisode) {
        if (!_watchedEpisodes.value.any { it.id == episode.id }) {
            _watchedEpisodes.value = _watchedEpisodes.value + episode
        }
    }
    
    override suspend fun markAsUnwatched(episode: WatchedEpisode) {
        _watchedEpisodes.value = _watchedEpisodes.value.filter { it.id != episode.id }
    }
    
    override suspend fun deleteAllForTvShow(tvShowId: String) {
        _watchedEpisodes.value = _watchedEpisodes.value.filter { it.tvShowId != tvShowId }
    }
}
