package org.lanzadera.proyectos.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.repository.SettingsRepository

class ObserveContentFiltersUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun observeShowMovies(): Flow<Boolean> = settingsRepository.observeShowMovies()

    fun observeShowSeries(): Flow<Boolean> = settingsRepository.observeShowSeries()
}
