package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository

class FakeWatchedEpisodesRepository : WatchedEpisodesRepository {
    
    private val watchedEpisodes = mutableMapOf<String, MutableList<WatchedEpisode>>()
    private val allEpisodesFlow = MutableStateFlow<List<WatchedEpisode>>(emptyList())
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>> {
        return MutableStateFlow(watchedEpisodes[tvShowId] ?: emptyList())
    }
    
    override fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>> = allEpisodesFlow
    
    override suspend fun toggleEpisodeWatched(episode: WatchedEpisode, isWatched: Boolean) {
        if (shouldFail) throw failureException
        
        val episodes = watchedEpisodes.getOrPut(episode.tvShowId) { mutableListOf() }
        if (isWatched) {
            if (!episodes.any { it.id == episode.id }) {
                episodes.add(episode)
            }
        } else {
            episodes.removeAll { it.id == episode.id }
        }
        updateAllEpisodes()
    }
    
    override suspend fun deleteAllForTvShow(tvShowId: String) {
        if (shouldFail) throw failureException
        watchedEpisodes.remove(tvShowId)
        updateAllEpisodes()
    }
    
    fun hasWatchedEpisode(tvShowId: String, episodeNum: Int): Boolean {
        return watchedEpisodes[tvShowId]?.any { it.episodeNumber == episodeNum } == true
    }
    
    fun markEpisodeAsWatched(tvShowId: String, episodeNum: Int) {
        val episodes = watchedEpisodes.getOrPut(tvShowId) { mutableListOf() }
        val episode = WatchedEpisode(
            tvShowId = tvShowId,
            seasonNumber = 1,
            episodeNumber = episodeNum
        )
        if (!episodes.any { it.id == episode.id }) {
            episodes.add(episode)
        }
        updateAllEpisodes()
    }
    
    private fun updateAllEpisodes() {
        allEpisodesFlow.value = watchedEpisodes.values.flatten()
    }
}
