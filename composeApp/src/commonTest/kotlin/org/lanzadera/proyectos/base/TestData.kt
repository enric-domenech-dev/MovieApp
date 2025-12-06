package org.lanzadera.proyectos.base

import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow

/**
 * Test data factory providing sample domain objects for tests.
 * 
 * Usage:
 * ```kotlin
 * @Test
 * fun `test with sample data`() {
 *     val movie = TestData.testMovie
 *     val tvShow = TestData.testTvShow()
 * }
 * ```
 */
object TestData {
    
    // Movies
    val testMovie = Movie(
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = listOf(28, 12, 14),
        id = 123,
        originalLanguage = "en",
        originalTitle = "Test Movie Original",
        overview = "This is a test movie overview for testing purposes.",
        popularity = 850.5,
        posterPath = "/poster.jpg",
        releaseDate = "2024-01-15",
        title = "Test Movie",
        video = false,
        voteAverage = 8.5,
        voteCount = 1500
    )
    
    val testMovie2 = Movie(
        adult = false,
        backdropPath = "/backdrop2.jpg",
        genreIds = listOf(35, 10749),
        id = 456,
        originalLanguage = "en",
        originalTitle = "Another Test Movie",
        overview = "Another test movie for testing.",
        popularity = 650.0,
        posterPath = "/poster2.jpg",
        releaseDate = "2024-02-20",
        title = "Test Movie 2",
        video = false,
        voteAverage = 7.8,
        voteCount = 980
    )
    
    // TV Shows
    val testTvShow = TvShow(
        adult = false,
        backdropPath = "/tv_backdrop.jpg",
        genreIds = listOf(18, 10765),
        id = 789,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = "Test TV Show Original",
        overview = "This is a test TV show overview.",
        popularity = 920.0,
        posterPath = "/tv_poster.jpg",
        firstAirDate = "2024-03-01",
        name = "Test TV Show",
        voteAverage = 8.9,
        voteCount = 2100
    )
    
    val testTvShow2 = TvShow(
        adult = false,
        backdropPath = "/tv_backdrop2.jpg",
        genreIds = listOf(35, 10759),
        id = 101,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = "Another Test Show",
        overview = "Another test TV show.",
        popularity = 750.0,
        posterPath = "/tv_poster2.jpg",
        firstAirDate = "2024-04-10",
        name = "Test TV Show 2",
        voteAverage = 8.2,
        voteCount = 1650
    )
    
    // Books
    val testBook = Book(
        id = "book123",
        title = "Test Book Title",
        authors = listOf("Test Author"),
        description = "This is a test book description for testing purposes.",
        publisher = "Test Publisher",
        publishedDate = "2024",
        pageCount = 350,
        categories = listOf("Fiction", "Adventure"),
        averageRating = 4.5,
        ratingsCount = 500,
        imageLinks = mapOf("thumbnail" to "https://example.com/book_cover.jpg"),
        previewLink = "https://example.com/preview",
        infoLink = "https://example.com/info"
    )
    
    val testBook2 = Book(
        id = "book456",
        title = "Another Test Book",
        authors = listOf("Another Author"),
        description = "Another test book description.",
        publisher = "Another Publisher",
        publishedDate = "2023",
        pageCount = 280,
        categories = listOf("Science", "Technology"),
        averageRating = 4.2,
        ratingsCount = 320,
        imageLinks = mapOf("thumbnail" to "https://example.com/book_cover2.jpg"),
        previewLink = "https://example.com/preview2",
        infoLink = "https://example.com/info2"
    )
    
    // Games
    val testGame = Game(
        id = 999,
        name = "Test Game",
        summary = "This is a test game summary for testing purposes.",
        cover = mapOf("url" to "//images.igdb.com/test_cover.jpg"),
        rating = 85.5,
        releaseDate = 1704067200L, // 2024-01-01
        genres = listOf(mapOf("id" to 12, "name" to "Role-playing (RPG)")),
        platforms = listOf(mapOf("id" to 6, "name" to "PC (Microsoft Windows)"))
    )
    
    val testGame2 = Game(
        id = 888,
        name = "Another Test Game",
        summary = "Another test game summary.",
        cover = mapOf("url" to "//images.igdb.com/test_cover2.jpg"),
        rating = 78.0,
        releaseDate = 1706745600L, // 2024-02-01
        genres = listOf(mapOf("id" to 5, "name" to "Shooter")),
        platforms = listOf(mapOf("id" to 48, "name" to "PlayStation 4"))
    )
    
    // Favorites
    val testFavoriteMovie = FavoriteItem(
        id = "123",
        type = FavoriteType.MOVIE,
        title = "Test Movie",
        posterPath = "/poster.jpg"
    )
    
    val testFavoriteTvShow = FavoriteItem(
        id = "789",
        type = FavoriteType.TV_SHOW,
        title = "Test TV Show",
        posterPath = "/tv_poster.jpg"
    )
    
    val testFavoriteBook = FavoriteItem(
        id = "book123",
        type = FavoriteType.BOOK,
        title = "Test Book Title",
        posterPath = "https://example.com/book_cover.jpg"
    )
    
    val testFavoriteGame = FavoriteItem(
        id = "999",
        type = FavoriteType.GAME,
        title = "Test Game",
        posterPath = "//images.igdb.com/test_cover.jpg"
    )
    
    // Helper functions to create custom test data
    fun createMovie(
        id: Int = 123,
        title: String = "Test Movie",
        overview: String = "Test overview",
        voteAverage: Double = 8.0
    ) = Movie(
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = listOf(28),
        id = id,
        originalLanguage = "en",
        originalTitle = title,
        overview = overview,
        popularity = 500.0,
        posterPath = "/poster.jpg",
        releaseDate = "2024-01-01",
        title = title,
        video = false,
        voteAverage = voteAverage,
        voteCount = 100
    )
    
    fun createTvShow(
        id: Int = 789,
        name: String = "Test TV Show",
        overview: String = "Test overview",
        voteAverage: Double = 8.0
    ) = TvShow(
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = listOf(18),
        id = id,
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = name,
        overview = overview,
        popularity = 500.0,
        posterPath = "/poster.jpg",
        firstAirDate = "2024-01-01",
        name = name,
        voteAverage = voteAverage,
        voteCount = 100
    )
    
    fun createFavoriteItem(
        id: String = "123",
        type: FavoriteType = FavoriteType.MOVIE,
        title: String = "Test Item"
    ) = FavoriteItem(
        id = id,
        type = type,
        title = title,
        posterPath = "/image.jpg"
    )
}
