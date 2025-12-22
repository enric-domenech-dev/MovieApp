package org.lanzadera.proyectos.domain.usecase.settings

import org.lanzadera.proyectos.domain.repository.SettingsRepository

class UpdateContentFiltersUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend fun updateShowMovies(value: Boolean) {
        settingsRepository.updateShowMovies(value)
    }

    suspend fun updateShowSeries(value: Boolean) {
        settingsRepository.updateShowSeries(value)
    }
}
