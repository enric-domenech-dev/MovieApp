package org.lanzadera.proyectos.data.storage.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val originalTitle: String?,
    val overview: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val runtime: Int?,
    val status: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val popularity: Double?,
    val budget: Int?,
    val revenue: Int?,
    val addedAt: Long,
    val updatedAt: Long
)
