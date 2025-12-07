package org.lanzadera.proyectos.data.datasource

actual fun createFavoritesLocalDataSource(): FavoritesLocalDataSource = InMemoryFavoritesLocalDataSource()

