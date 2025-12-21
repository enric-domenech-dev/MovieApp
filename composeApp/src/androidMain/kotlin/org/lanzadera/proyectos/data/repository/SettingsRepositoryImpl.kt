package org.lanzadera.proyectos.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lanzadera.proyectos.data.storage.room.FavoritesDatabase
import org.lanzadera.proyectos.data.storage.room.SettingsEntity
import org.lanzadera.proyectos.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val database: FavoritesDatabase
) : SettingsRepository {

    private val settingsDao = database.settingsDao()

    private suspend fun ensureSettingsExist() {
        if (settingsDao.getSettings() == null) {
            settingsDao.saveSettings(SettingsEntity())
        }
    }

    override fun observeShowAvailableSeries(): Flow<Boolean> {
        return settingsDao.observeSettings().map { it?.showAvailableSeries ?: true }
    }

    override fun observeShowUpcomingSeries(): Flow<Boolean> {
        return settingsDao.observeSettings().map { it?.showUpcomingSeries ?: true }
    }

    override fun observeShowInProductionSeries(): Flow<Boolean> {
        return settingsDao.observeSettings().map { it?.showInProductionSeries ?: true }
    }

    override fun observeShowEndedSeries(): Flow<Boolean> {
        return settingsDao.observeSettings().map { it?.showEndedSeries ?: true }
    }

    override fun observeShowAvailableMovies(): Flow<Boolean> {
        return settingsDao.observeSettings().map { it?.showAvailableMovies ?: true }
    }

    override fun observeShowUpcomingMovies(): Flow<Boolean> {
        return settingsDao.observeSettings().map { it?.showUpcomingMovies ?: true }
    }

    override suspend fun updateShowAvailableSeries(value: Boolean) {
        ensureSettingsExist()
        settingsDao.updateShowAvailableSeries(value)
    }

    override suspend fun updateShowUpcomingSeries(value: Boolean) {
        ensureSettingsExist()
        settingsDao.updateShowUpcomingSeries(value)
    }

    override suspend fun updateShowInProductionSeries(value: Boolean) {
        ensureSettingsExist()
        settingsDao.updateShowInProductionSeries(value)
    }

    override suspend fun updateShowEndedSeries(value: Boolean) {
        ensureSettingsExist()
        settingsDao.updateShowEndedSeries(value)
    }

    override suspend fun updateShowAvailableMovies(value: Boolean) {
        ensureSettingsExist()
        settingsDao.updateShowAvailableMovies(value)
    }

    override suspend fun updateShowUpcomingMovies(value: Boolean) {
        ensureSettingsExist()
        settingsDao.updateShowUpcomingMovies(value)
    }
}
