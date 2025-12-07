package org.lanzadera.proyectos.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lanzadera.proyectos.data.storage.room.FavoritesDatabase
import org.lanzadera.proyectos.data.storage.room.WatchedEpisodeEntity
import org.lanzadera.proyectos.domain.models.WatchedEpisode

class RoomWatchedEpisodesDataSource : WatchedEpisodesDataSource {
    private val dao = FavoritesDatabase.instance.watchedEpisodeDao()

    override fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisode>> {
        return dao.observeWatchedEpisodes(tvShowId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisode>> {
        return dao.observeAllWatchedEpisodes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getWatchedEpisodes(tvShowId: String): List<WatchedEpisode> {
        return dao.getWatchedEpisodes(tvShowId).map { it.toDomain() }
    }

    override suspend fun markAsWatched(episode: WatchedEpisode) {
        dao.insert(episode.toEntity())
    }

    override suspend fun markAsUnwatched(episode: WatchedEpisode) {
        dao.deleteById(episode.id)
    }

    override suspend fun deleteAllForTvShow(tvShowId: String) {
        dao.deleteByTvShowId(tvShowId)
    }

    private fun WatchedEpisodeEntity.toDomain() = WatchedEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    )

    private fun WatchedEpisode.toEntity() = WatchedEpisodeEntity(
        id = id,
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        watchedAt = System.currentTimeMillis()
    )
}
