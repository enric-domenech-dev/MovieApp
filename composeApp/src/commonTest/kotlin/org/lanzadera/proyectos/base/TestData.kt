package org.lanzadera.proyectos.base

import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType

/**
 * Test data factory providing sample domain objects for tests.
 */
object TestData {
    
    // Favorites
    val testFavoriteMovie = FavoriteItem(
        id = "123",
        type = FavoriteType.MOVIE,
        title = "Test Movie",
        posterUrl = "/poster.jpg"
    )
    
    val testFavoriteTvShow = FavoriteItem(
        id = "789",
        type = FavoriteType.TV_SHOW,
        title = "Test TV Show",
        posterUrl = "/tv_poster.jpg"
    )
    
    val testFavoriteBook = FavoriteItem(
        id = "book123",
        type = FavoriteType.BOOK,
        title = "Test Book Title",
        posterUrl = "https://example.com/book_cover.jpg"
    )
    
    val testFavoriteGame = FavoriteItem(
        id = "999",
        type = FavoriteType.GAME,
        title = "Test Game",
        posterUrl = "//images.igdb.com/test_cover.jpg"
    )
    
    // Helper functions to create custom test data
    fun createFavoriteItem(
        id: String = "123",
        type: FavoriteType = FavoriteType.MOVIE,
        title: String = "Test Item",
        posterUrl: String? = "/image.jpg"
    ) = FavoriteItem(
        id = id,
        type = type,
        title = title,
        posterUrl = posterUrl
    )
}
