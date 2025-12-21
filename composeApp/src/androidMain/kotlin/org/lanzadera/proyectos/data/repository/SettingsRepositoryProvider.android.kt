package org.lanzadera.proyectos.data.repository

import org.lanzadera.proyectos.data.storage.room.FavoritesDatabase
import org.lanzadera.proyectos.domain.repository.SettingsRepository

actual fun createSettingsRepository(): SettingsRepository {
    return SettingsRepositoryImpl(FavoritesDatabase.instance)
}
