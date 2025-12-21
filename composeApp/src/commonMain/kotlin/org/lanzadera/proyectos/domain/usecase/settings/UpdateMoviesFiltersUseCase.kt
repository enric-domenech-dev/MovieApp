package org.lanzadera.proyectos.domain.usecase.settings

import org.lanzadera.proyectos.domain.repository.SettingsRepository

class UpdateMoviesFiltersUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend fun updateShowAvailableMovies(value: Boolean) {
        settingsRepository.updateShowAvailableMovies(value)
    }

    suspend fun updateShowUpcomingMovies(value: Boolean) {
        settingsRepository.updateShowUpcomingMovies(value)
    }
}
