package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.fakes.FakeFavoriteDetailsRepository
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import org.lanzadera.proyectos.fakes.FakeMovieRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ToggleMovieFavoriteUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ToggleMovieFavoriteUseCase
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    private lateinit var fakeFavoriteDetailsRepository: FakeFavoriteDetailsRepository
    private lateinit var fakeMovieRepository: FakeMovieRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeFavoritesRepository = FakeFavoritesRepository()
        fakeFavoriteDetailsRepository = FakeFavoriteDetailsRepository()
        fakeMovieRepository = FakeMovieRepository()
        useCase = ToggleMovieFavoriteUseCase(
            fakeFavoritesRepository,
            fakeFavoriteDetailsRepository,
            fakeMovieRepository
        )
    }
    
    @Test
    fun `should add movie to favorites and save details`() = runTest {
        // Given
        val movieItem = FavoriteItem(
            id = "123",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // Setup fake movie details
        val movieDetails = org.lanzadera.proyectos.domain.models.movie.Movie(
            id = 123,
            title = "Test Movie",
            releaseDate = "2024-01-01"
        )
        fakeMovieRepository.setMovieDetails(123, movieDetails)
        
        // When
        val result = useCase(movieItem)
        
        // Then
        assertTrue(result.isSuccess)
        
        // Verify movie added to favorites
        assertTrue(fakeFavoritesRepository.isFavorite("123"))
        
        // Verify full details saved
        val savedMovie = fakeFavoriteDetailsRepository.getFavoriteMovie("123")
        assertNotNull(savedMovie)
        assertEquals(123, savedMovie.id)
    }
    
    @Test
    fun `should remove movie from favorites and delete details`() = runTest {
        // Given - Movie already favorited
        val movieItem = FavoriteItem(
            id = "123",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        fakeFavoritesRepository.addFavorite(movieItem)
        
        // When - Toggle to remove
        val result = useCase(movieItem)
        
        // Then
        assertTrue(result.isSuccess)
        
        // Verify movie removed from favorites
        assertFalse(fakeFavoritesRepository.isFavorite("123"))
        
        // Verify details deleted
        val savedMovie = fakeFavoriteDetailsRepository.getFavoriteMovie("123")
        assertNull(savedMovie)
    }
    
    @Test
    fun `should toggle movie multiple times`() = runTest {
        // Given
        val movieItem = FavoriteItem(
            id = "456",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When - Add
        val result1 = useCase(movieItem)
        assertTrue(result1.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("456"))
        
        // When - Remove
        val result2 = useCase(movieItem)
        assertTrue(result2.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite("456"))
        
        // When - Add again
        val result3 = useCase(movieItem)
        assertTrue(result3.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("456"))
    }
    
    @Test
    fun `should handle invalid movie ID gracefully`() = runTest {
        // Given - Invalid ID (not a number)
        val movieItem = FavoriteItem(
            id = "invalid",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When
        val result = useCase(movieItem)
        
        // Then - Should still succeed (toggle happens, just no details saved)
        assertTrue(result.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("invalid"))
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val movieItem = FavoriteItem(
            id = "123",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        fakeFavoritesRepository.shouldFail = true
        fakeFavoritesRepository.failureException = Exception("Database error")
        
        // When
        val result = useCase(movieItem)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `should throw IllegalArgumentException when type is not MOVIE`() = runTest {
        // Given - Wrong type
        val tvShowItem = FavoriteItem(
            id = "123",
            title = "Test TV Show",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When/Then
        try {
            useCase(tvShowItem)
            throw AssertionError("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Item must be of type MOVIE") == true)
        }
    }
}
