package org.lanzadera.proyectos.data.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTvShowDao {
    @Query("SELECT * FROM favorite_tv_shows ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<FavoriteTvShowEntity>>

    @Query("SELECT * FROM favorite_tv_shows WHERE id = :id")
    suspend fun getById(id: String): FavoriteTvShowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FavoriteTvShowEntity)

    @Query("DELETE FROM favorite_tv_shows WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM favorite_tv_shows")
    suspend fun deleteAll()
}
