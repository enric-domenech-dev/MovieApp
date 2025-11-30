package org.lanzadera.proyectos.data.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedMovieDao {
    @Query("SELECT * FROM watched_movies")
    fun observeAll(): Flow<List<WatchedMovieEntity>>

    @Query("SELECT * FROM watched_movies WHERE movieId = :movieId")
    suspend fun getByMovieId(movieId: String): WatchedMovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchedMovieEntity)

    @Query("DELETE FROM watched_movies WHERE movieId = :movieId")
    suspend fun deleteByMovieId(movieId: String)

    @Query("DELETE FROM watched_movies")
    suspend fun deleteAll()
}
