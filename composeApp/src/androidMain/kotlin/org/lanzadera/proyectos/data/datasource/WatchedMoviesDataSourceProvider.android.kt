package org.lanzadera.proyectos.data.datasource

actual fun createWatchedMoviesDataSource(): WatchedMoviesDataSource {
    return RoomWatchedMoviesDataSource()
}
