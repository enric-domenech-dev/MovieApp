package org.lanzadera.proyectos.data.datasource

import kotlinx.serialization.json.Json
import org.lanzadera.proyectos.data.repository.FavoriteDetailsRepositoryImpl
import org.lanzadera.proyectos.data.storage.room.FavoritesDatabase
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository

actual fun createFavoriteDetailsRepository(): FavoriteDetailsRepository {
    val database = FavoritesDatabase.instance
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }
    return FavoriteDetailsRepositoryImpl(
        tvShowDao = database.favoriteTvShowDao(),
        movieDao = database.favoriteMovieDao(),
        json = json
    )
}
