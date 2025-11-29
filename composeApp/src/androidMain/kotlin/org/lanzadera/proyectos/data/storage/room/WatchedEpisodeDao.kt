package org.lanzadera.proyectos.data.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedEpisodeDao {
    @Query("SELECT * FROM watched_episodes WHERE tvShowId = :tvShowId")
    fun observeWatchedEpisodes(tvShowId: String): Flow<List<WatchedEpisodeEntity>>

    @Query("SELECT * FROM watched_episodes")
    fun observeAllWatchedEpisodes(): Flow<List<WatchedEpisodeEntity>>

    @Query("SELECT * FROM watched_episodes WHERE tvShowId = :tvShowId")
    suspend fun getWatchedEpisodes(tvShowId: String): List<WatchedEpisodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: WatchedEpisodeEntity)

    @Query("DELETE FROM watched_episodes WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM watched_episodes WHERE tvShowId = :tvShowId")
    suspend fun deleteByTvShowId(tvShowId: String)

    @Query("DELETE FROM watched_episodes")
    suspend fun deleteAll()
}
