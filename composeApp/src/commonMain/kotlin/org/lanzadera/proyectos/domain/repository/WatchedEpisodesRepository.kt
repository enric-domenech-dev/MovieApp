package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.models.WatchedEpisode

/**
 * Repository for managing watched episode status.
 *
 * Tracks which TV show episodes the user has marked as watched.
 */
interface WatchedEpisodesRepository {
    /**
     * Observes watched episodes for a specific TV show.
     * @param tvShowId The TV show ID
     * @return Flow emitting list of watched episodes for the show
     */
    fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>>
    
    /**
     * Observes all watched episodes across all TV shows.
     * @return Flow emitting list of all watched episodes
     */
    fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>>
    
    /**
     * Toggles watched status for an episode.
     * @param episode The episode to toggle
     * @param isWatched True to mark as watched, false to unmark
     */
    suspend fun toggleEpisodeWatched(episode: WatchedEpisode, isWatched: Boolean)
    
    /**
     * Deletes all watched episodes for a TV show.
     * Used when removing a show from favorites.
     * @param tvShowId The TV show ID
     */
    suspend fun deleteAllForTvShow(tvShowId: String)
}
