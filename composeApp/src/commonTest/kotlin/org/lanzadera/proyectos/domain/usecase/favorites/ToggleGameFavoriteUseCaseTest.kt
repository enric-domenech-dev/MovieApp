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

class ToggleGameFavoriteUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ToggleGameFavoriteUseCase
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeFavoritesRepository = FakeFavoritesRepository()
        useCase = ToggleGameFavoriteUseCase(fakeFavoritesRepository)
    }
    
    @Test
    fun `should add game to favorites`() = runTest {
        // Given
        val gameItem = FavoriteItem(
            id = "game123",
            title = "Test Game",
            type = FavoriteType.GAME,
            posterUrl = "https://image.com/cover.jpg"
        )
        
        // When
        val result = useCase(gameItem)
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("game123"))
    }
    
    @Test
    fun `should remove game from favorites`() = runTest {
        // Given - Game already favorited
        val gameItem = FavoriteItem(
            id = "game123",
            title = "Test Game",
            type = FavoriteType.GAME,
            posterUrl = "https://image.com/cover.jpg"
        )
        fakeFavoritesRepository.addFavorite(gameItem)
        
        // When - Toggle to remove
        val result = useCase(gameItem)
        
        // Then
        assertTrue(result.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite("game123"))
    }
    
    @Test
    fun `should toggle game multiple times`() = runTest {
        // Given
        val gameItem = FavoriteItem(
            id = "game456",
            title = "Test Game",
            type = FavoriteType.GAME,
            posterUrl = "https://image.com/cover.jpg"
        )
        
        // When - Add
        val result1 = useCase(gameItem)
        assertTrue(result1.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("game456"))
        
        // When - Remove
        val result2 = useCase(gameItem)
        assertTrue(result2.isSuccess)
        assertFalse(fakeFavoritesRepository.isFavorite("game456"))
        
        // When - Add again
        val result3 = useCase(gameItem)
        assertTrue(result3.isSuccess)
        assertTrue(fakeFavoritesRepository.isFavorite("game456"))
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val gameItem = FavoriteItem(
            id = "game123",
            title = "Test Game",
            type = FavoriteType.GAME,
            posterUrl = "https://image.com/cover.jpg"
        )
        fakeFavoritesRepository.shouldFail = true
        fakeFavoritesRepository.failureException = Exception("Database error")
        
        // When
        val result = useCase(gameItem)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `should throw IllegalArgumentException when type is not GAME`() = runTest {
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
            assertTrue(e.message?.contains("Item must be of type GAME") == true)
        }
    }
}
