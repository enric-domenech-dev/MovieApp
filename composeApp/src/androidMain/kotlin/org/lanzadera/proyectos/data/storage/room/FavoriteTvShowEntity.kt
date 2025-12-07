package org.lanzadera.proyectos.data.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tv_shows")
data class FavoriteTvShowEntity(
    @PrimaryKey val id: String,
    val name: String,
    val originalName: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val firstAirDate: String?,
    val lastAirDate: String?,
    val numberOfSeasons: Int?,
    val numberOfEpisodes: Int?,
    val status: String?,
    val inProduction: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val popularity: Double?,
    val addedAt: Long,
    val updatedAt: Long,
    // Almacenar episodios como JSON string
    val seasonsJson: String?
)
