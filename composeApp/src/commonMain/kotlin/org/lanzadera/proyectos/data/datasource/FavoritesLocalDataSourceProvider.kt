package org.lanzadera.proyectos.data.datasource

/**
 * Platform hook that provides the concrete implementation of [FavoritesLocalDataSource].
 */
expect fun createFavoritesLocalDataSource(): FavoritesLocalDataSource

