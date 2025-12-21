package org.lanzadera.proyectos.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.repository.SettingsRepository

class ObserveSeriesFiltersUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun observeShowAvailableSeries(): Flow<Boolean> =
        settingsRepository.observeShowAvailableSeries()

    fun observeShowUpcomingSeries(): Flow<Boolean> =
        settingsRepository.observeShowUpcomingSeries()

    fun observeShowInProductionSeries(): Flow<Boolean> =
        settingsRepository.observeShowInProductionSeries()

    fun observeShowEndedSeries(): Flow<Boolean> =
        settingsRepository.observeShowEndedSeries()
}
