package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository para gestionar las preferencias de filtros de favoritos.
 */
interface SettingsRepository {
    fun observeShowAvailableSeries(): Flow<Boolean>
    fun observeShowUpcomingSeries(): Flow<Boolean>
    fun observeShowInProductionSeries(): Flow<Boolean>
    fun observeShowEndedSeries(): Flow<Boolean>
    fun observeShowAvailableMovies(): Flow<Boolean>
    fun observeShowUpcomingMovies(): Flow<Boolean>

    // Observe content type chips (movies / series)
    fun observeShowMovies(): Flow<Boolean>
    fun observeShowSeries(): Flow<Boolean>

    suspend fun updateShowAvailableSeries(value: Boolean)
    suspend fun updateShowUpcomingSeries(value: Boolean)
    suspend fun updateShowInProductionSeries(value: Boolean)
    suspend fun updateShowEndedSeries(value: Boolean)
    suspend fun updateShowAvailableMovies(value: Boolean)
    suspend fun updateShowUpcomingMovies(value: Boolean)

    // Update content type chips
    suspend fun updateShowMovies(value: Boolean)
    suspend fun updateShowSeries(value: Boolean)
}
