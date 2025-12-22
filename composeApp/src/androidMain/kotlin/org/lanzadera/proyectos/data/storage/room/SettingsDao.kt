package org.lanzadera.proyectos.data.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1")
    fun observeSettings(): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettings(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)

    @Query("UPDATE settings SET showAvailableSeries = :value WHERE id = 1")
    suspend fun updateShowAvailableSeries(value: Boolean)

    @Query("UPDATE settings SET showUpcomingSeries = :value WHERE id = 1")
    suspend fun updateShowUpcomingSeries(value: Boolean)

    @Query("UPDATE settings SET showInProductionSeries = :value WHERE id = 1")
    suspend fun updateShowInProductionSeries(value: Boolean)

    @Query("UPDATE settings SET showEndedSeries = :value WHERE id = 1")
    suspend fun updateShowEndedSeries(value: Boolean)

    @Query("UPDATE settings SET showAvailableMovies = :value WHERE id = 1")
    suspend fun updateShowAvailableMovies(value: Boolean)

    @Query("UPDATE settings SET showUpcomingMovies = :value WHERE id = 1")
    suspend fun updateShowUpcomingMovies(value: Boolean)

    @Query("UPDATE settings SET showMovies = :value WHERE id = 1")
    suspend fun updateShowMovies(value: Boolean)

    @Query("UPDATE settings SET showSeries = :value WHERE id = 1")
    suspend fun updateShowSeries(value: Boolean)
}
