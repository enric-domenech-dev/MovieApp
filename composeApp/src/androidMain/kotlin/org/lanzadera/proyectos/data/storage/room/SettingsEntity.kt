package org.lanzadera.proyectos.data.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int = 1, // Siempre 1, solo hay una fila de settings
    val showAvailableSeries: Boolean = true,
    val showUpcomingSeries: Boolean = true,
    val showInProductionSeries: Boolean = true,
    val showEndedSeries: Boolean = true,
    val showAvailableMovies: Boolean = true,
    val showUpcomingMovies: Boolean = true
)
