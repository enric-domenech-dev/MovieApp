package org.lanzadera.proyectos.data.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_movies")
data class WatchedMovieEntity(
    @PrimaryKey val id: String,
    val movieId: String,
    val watchedAt: Long
)
