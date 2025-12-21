package org.lanzadera.proyectos.domain.usecase.settings

import org.lanzadera.proyectos.domain.repository.SettingsRepository

class UpdateSeriesFiltersUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend fun updateShowAvailableSeries(value: Boolean) {
        settingsRepository.updateShowAvailableSeries(value)
    }

    suspend fun updateShowUpcomingSeries(value: Boolean) {
        settingsRepository.updateShowUpcomingSeries(value)
    }

    suspend fun updateShowInProductionSeries(value: Boolean) {
        settingsRepository.updateShowInProductionSeries(value)
    }

    suspend fun updateShowEndedSeries(value: Boolean) {
        settingsRepository.updateShowEndedSeries(value)
    }
}
