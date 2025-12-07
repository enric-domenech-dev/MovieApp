package org.lanzadera.proyectos.data.datasource

actual fun createWatchedEpisodesDataSource(): WatchedEpisodesDataSource {
    return RoomWatchedEpisodesDataSource()
}
