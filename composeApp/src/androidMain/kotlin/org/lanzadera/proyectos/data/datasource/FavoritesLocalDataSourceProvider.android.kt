package org.lanzadera.proyectos.data.datasource

import org.lanzadera.proyectos.data.storage.room.FavoritesDatabase

actual fun createFavoritesLocalDataSource(): FavoritesLocalDataSource {
    val dao = FavoritesDatabase.instance.favoriteDao()
    return RoomFavoritesLocalDataSource(dao)
}
