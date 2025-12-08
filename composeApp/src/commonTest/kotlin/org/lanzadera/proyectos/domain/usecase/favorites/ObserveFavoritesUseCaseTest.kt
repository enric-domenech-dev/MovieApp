package org.lanzadera.proyectos.domain.usecase.favorites

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.fakes.FakeFavoritesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObserveFavoritesUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ObserveFavoritesUseCase
    private lateinit var fakeFavoritesRepository: FakeFavoritesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeFavoritesRepository = FakeFavoritesRepository()
        useCase = ObserveFavoritesUseCase(fakeFavoritesRepository)
    }
    
    @Test
    fun `should emit empty list initially`() = runTest {
        // When
        useCase().test {
            // Then
            val favorites = awaitItem()
            assertTrue(favorites.isEmpty())
        }
    }
    
    @Test
    fun `should emit favorites when added`() = runTest {
        // Given
        val movieItem = FavoriteItem(
            id = "123",
            title = "Test Movie",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster.jpg"
        )
        
        // When
        useCase().test {
            // Initially empty
            assertTrue(awaitItem().isEmpty())
            
            // Add favorite
            fakeFavoritesRepository.addFavorite(movieItem)
            
            // Then - Should emit updated list
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals("123", favorites[0].id)
            assertEquals("Test Movie", favorites[0].title)
        }
    }
    
    @Test
    fun `should emit multiple favorites`() = runTest {
        // Given
        val movieItem = FavoriteItem(
            id = "123",
            title = "Movie 1",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster1.jpg"
        )
        val tvShowItem = FavoriteItem(
            id = "456",
            title = "TV Show 1",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster2.jpg"
        )
        
        // When
        useCase().test {
            // Initially empty
            assertTrue(awaitItem().isEmpty())
            
            // Add favorites
            fakeFavoritesRepository.addFavorite(movieItem)
            assertEquals(1, awaitItem().size)
            
            fakeFavoritesRepository.addFavorite(tvShowItem)
            
            // Then
            val favorites = awaitItem()
            assertEquals(2, favorites.size)
        }
    }
    
    @Test
    fun `should emit updated list when favorite removed`() = runTest {
        // Given - Two favorites
        val movieItem = FavoriteItem(
            id = "123",
            title = "Movie 1",
            type = FavoriteType.MOVIE,
            posterUrl = "https://image.com/poster1.jpg"
        )
        val tvShowItem = FavoriteItem(
            id = "456",
            title = "TV Show 1",
            type = FavoriteType.TV_SHOW,
            posterUrl = "https://image.com/poster2.jpg"
        )
        fakeFavoritesRepository.addFavorite(movieItem)
        fakeFavoritesRepository.addFavorite(tvShowItem)
        
        // When
        useCase().test {
            // Initial state with 2 favorites
            assertEquals(2, awaitItem().size)
            
            // Remove one (toggle removes if already exists)
            fakeFavoritesRepository.toggleFavorite(movieItem)
            
            // Then - Should emit updated list with 1 item
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals("456", favorites[0].id)
        }
    }
}
