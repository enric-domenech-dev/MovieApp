package org.lanzadera.proyectos.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import org.lanzadera.proyectos.domain.repository.SettingsRepository

class ObserveMoviesFiltersUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun observeShowAvailableMovies(): Flow<Boolean> =
        settingsRepository.observeShowAvailableMovies()

    fun observeShowUpcomingMovies(): Flow<Boolean> =
        settingsRepository.observeShowUpcomingMovies()
}
