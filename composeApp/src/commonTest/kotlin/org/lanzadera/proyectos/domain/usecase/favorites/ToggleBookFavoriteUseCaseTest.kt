package org.lanzadera.proyectos.domain.usecase.favorites

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToggleBookFavoriteUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ToggleBookFavoriteUseCase
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeFavoritesRepository = FakeFavoritesRepository()
        useCase = ToggleBookFavoriteUseCase(fakeFavoritesRepository)
    }
    
    @Test
    fun `should add book to favorites`() = runTest {
        // Given
        val bookItem = FavoriteItem(
            id = "book123",
            title = "Test Book",
            type = FavoriteType.BOOK,
            posterUrl = "https://image.com/cover.jpg"
        )
        
        // When
        val result = useCase(bookItem)
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("book123"))
    }
    
    @Test
    fun `should remove book from favorites`() = runTest {
        // Given - Book already favorited
        val bookItem = FavoriteItem(
            id = "book123",
            title = "Test Book",
            type = FavoriteType.BOOK,
            posterUrl = "https://image.com/cover.jpg"
        )
        fakeFavoritesRepository.addFavorite(bookItem)
        
        // When - Toggle to remove
        val result = useCase(bookItem)
        
        // Then
        assertTrue(result.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite("book123"))
    }
    
    @Test
    fun `should toggle book multiple times`() = runTest {
        // Given
        val bookItem = FavoriteItem(
            id = "book456",
            title = "Test Book",
            type = FavoriteType.BOOK,
            posterUrl = "https://image.com/cover.jpg"
        )
        
        // When - Add
        val result1 = useCase(bookItem)
        assertTrue(result1.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("book456"))
        
        // When - Remove
        val result2 = useCase(bookItem)
        assertTrue(result2.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite("book456"))
        
        // When - Add again
        val result3 = useCase(bookItem)
        assertTrue(result3.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("book456"))
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val bookItem = FavoriteItem(
            id = "book123",
            title = "Test Book",
            type = FavoriteType.BOOK,
            posterUrl = "https://image.com/cover.jpg"
        )
        fakeFavoritesRepository.shouldFail = true
        fakeFavoritesRepository.failureException = Exception("Database error")
        
        // When
        val result = useCase(bookItem)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `should throw IllegalArgumentException when type is not BOOK`() = runTest {
        // Given - Wrong type
        val movieItem = FavoriteItem(
            id = "123",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When/Then
        try {
            useCase(movieItem)
            throw AssertionError("Expected IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message?.contains("Item must be of type BOOK") == true)
        }
    }
}
