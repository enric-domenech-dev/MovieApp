package org.lanzadera.proyectos.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.lanzadera.proyectos.domain.repository.SettingsRepository

class FakeSettingsRepository : SettingsRepository {
    private val _showAvailableSeries = MutableStateFlow(true)
    private val _showUpcomingSeries = MutableStateFlow(true)
    private val _showInProductionSeries = MutableStateFlow(true)
    private val _showEndedSeries = MutableStateFlow(true)
    private val _showAvailableMovies = MutableStateFlow(true)
    private val _showUpcomingMovies = MutableStateFlow(true)

    private val _showMovies = MutableStateFlow(false)
    private val _showSeries = MutableStateFlow(true)

    override fun observeShowAvailableSeries(): Flow<Boolean> = _showAvailableSeries

    override fun observeShowUpcomingSeries(): Flow<Boolean> = _showUpcomingSeries

    override fun observeShowInProductionSeries(): Flow<Boolean> = _showInProductionSeries

    override fun observeShowEndedSeries(): Flow<Boolean> = _showEndedSeries

    override fun observeShowAvailableMovies(): Flow<Boolean> = _showAvailableMovies

    override fun observeShowUpcomingMovies(): Flow<Boolean> = _showUpcomingMovies

    override fun observeShowMovies(): Flow<Boolean> = _showMovies

    override fun observeShowSeries(): Flow<Boolean> = _showSeries

    override suspend fun updateShowAvailableSeries(value: Boolean) {
        _showAvailableSeries.value = value
    }

    override suspend fun updateShowUpcomingSeries(value: Boolean) {
        _showUpcomingSeries.value = value
    }

    override suspend fun updateShowInProductionSeries(value: Boolean) {
        _showInProductionSeries.value = value
    }

    override suspend fun updateShowEndedSeries(value: Boolean) {
        _showEndedSeries.value = value
    }

    override suspend fun updateShowAvailableMovies(value: Boolean) {
        _showAvailableMovies.value = value
    }

    override suspend fun updateShowUpcomingMovies(value: Boolean) {
        _showUpcomingMovies.value = value
    }

    override suspend fun updateShowMovies(value: Boolean) {
        _showMovies.value = value
    }

    override suspend fun updateShowSeries(value: Boolean) {
        _showSeries.value = value
    }
}
