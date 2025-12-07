package org.lanzadera.proyectos.data.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class RoomFavoriteEntity(
    @PrimaryKey val key: String,
    val id: String,
    val type: String,
    val title: String,
    val posterUrl: String?,
    val overview: String?,
    val addedAt: Long,
    val updatedAt: Long
)

