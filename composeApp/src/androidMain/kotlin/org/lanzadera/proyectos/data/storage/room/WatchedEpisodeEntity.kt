package org.lanzadera.proyectos.data.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_episodes")
data class WatchedEpisodeEntity(
    @PrimaryKey val id: String,
    val tvShowId: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val watchedAt: Long
)
