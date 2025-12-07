package org.lanzadera.proyectos.data.storage.room

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface FavoriteItemDao {
    @Query(
        """
        SELECT 
            id,
            'MOVIE' as type,
            title,
            posterPath,
            overview,
            addedAt,
            updatedAt
        FROM favorite_movies
    """
    )
    fun observeAllMovies(): Flow<List<FavoriteItemDto>>

    @Query(
        """
        SELECT 
            id,
            'TV_SHOW' as type,
            name as title,
            posterPath,
            overview,
            addedAt,
            updatedAt
        FROM favorite_tv_shows
    """
    )
    fun observeAllTvShows(): Flow<List<FavoriteItemDto>>
}

data class FavoriteItemDto(
    val id: String,
    val type: String,
    val title: String,
    val posterPath: String?,
    val overview: String?,
    val addedAt: Long,
    val updatedAt: Long
)

fun FavoriteItemDao.observeAllFavorites(): Flow<List<FavoriteItemDto>> {
    return observeAllMovies().combine(observeAllTvShows()) { movies, tvShows ->
        (movies + tvShows).sortedByDescending { it.updatedAt }
    }
}
