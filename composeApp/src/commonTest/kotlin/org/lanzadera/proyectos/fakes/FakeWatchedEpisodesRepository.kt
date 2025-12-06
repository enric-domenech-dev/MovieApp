package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository

class FakeWatchedEpisodesRepository : WatchedEpisodesRepository {
    
    private val watchedEpisodes = mutableMapOf<String, MutableSet<Int>>()
    
    var shouldFail = false
    var failureException = Exception("Test failure")
    
    override fun observeWatchedEpisodesForTvShow(tvShowId: String): Flow<Set<Int>> {
        return MutableStateFlow(watchedEpisodes[tvShowId] ?: emptySet())
    }
    
    override suspend fun toggleEpisodeWatched(tvShowId: String, episodeId: Int) {
        if (shouldFail) throw failureException
        
        val episodes = watchedEpisodes.getOrPut(tvShowId) { mutableSetOf() }
        if (episodes.contains(episodeId)) {
            episodes.remove(episodeId)
        } else {
            episodes.add(episodeId)
        }
    }
    
    override suspend fun markEpisodeAsWatched(tvShowId: String, episodeId: Int) {
        if (shouldFail) throw failureException
        
        watchedEpisodes.getOrPut(tvShowId) { mutableSetOf() }.add(episodeId)
    }
    
    override suspend fun markEpisodeAsUnwatched(tvShowId: String, episodeId: Int) {
        if (shouldFail) throw failureException
        
        watchedEpisodes[tvShowId]?.remove(episodeId)
    }
    
    override suspend fun deleteAllForTvShow(tvShowId: String) {
        if (shouldFail) throw failureException
        
        watchedEpisodes.remove(tvShowId)
    }
    
    fun hasWatchedEpisode(tvShowId: String, episodeId: Int): Boolean {
        return watchedEpisodes[tvShowId]?.contains(episodeId) == true
    }
}
