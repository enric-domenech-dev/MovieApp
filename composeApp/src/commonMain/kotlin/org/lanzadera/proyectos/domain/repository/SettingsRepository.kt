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

    suspend fun updateShowAvailableSeries(value: Boolean)
    suspend fun updateShowUpcomingSeries(value: Boolean)
    suspend fun updateShowInProductionSeries(value: Boolean)
    suspend fun updateShowEndedSeries(value: Boolean)
    suspend fun updateShowAvailableMovies(value: Boolean)
    suspend fun updateShowUpcomingMovies(value: Boolean)
}
