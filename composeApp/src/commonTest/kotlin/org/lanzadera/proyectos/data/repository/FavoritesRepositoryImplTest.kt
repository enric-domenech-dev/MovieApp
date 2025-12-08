package org.lanzadera.proyectos.data.repository

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.RepositoryTest
import org.lanzadera.proyectos.domain.favorites.FakeFavoritesLocalDataSource
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Comprehensive tests for FavoritesRepositoryImpl.
 * 
 * Tests toggle functionality, sync operations, and flow emissions.
 * 
 * Context: Favorites are used across all content types (movies, TV shows, books, games).
 */
class FavoritesRepositoryImplTest : RepositoryTest() {
    
    private lateinit var dataSource: FakeFavoritesLocalDataSource
    private lateinit var repository: FavoritesRepositoryImpl
    
    override fun setup() {
        super.setup()
        dataSource = FakeFavoritesLocalDataSource()
        repository = FavoritesRepositoryImpl(dataSource)
    }
    
    @Test
    fun `initial state is empty`() = runTest {
        // When
        repository.favorites.test {
            val items = awaitItem()
            
            // Then
            assertTrue(items.isEmpty(), "Initial favorites should be empty")
        }
    }
    
    @Test
    fun `toggleFavorite adds movie to favorites when not favorited`() = runTest {
        // Given
        val movie = FavoriteItem(
            id = "1",
            type = FavoriteType.MOVIE,
            title = "The Matrix"
        )
        
        // When
        repository.toggleFavorite(movie)
        
        // Then
        repository.favorites.test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals(movie.id, items[0].id)
            assertEquals(movie.type, items[0].type)
        }
    }
    
    @Test
    fun `toggleFavorite removes movie from favorites when already favorited`() = runTest {
        // Given
        val movie = FavoriteItem(
            id = "1",
            type = FavoriteType.MOVIE,
            title = "The Matrix"
        )
        repository.toggleFavorite(movie)
        
        // When
        repository.toggleFavorite(movie)
        
        // Then
        repository.favorites.test {
            val items = awaitItem()
            assertTrue(items.isEmpty(), "Movie should be removed from favorites")
        }
    }
    
    @Test
    fun `toggleFavorite adds TV show to favorites`() = runTest {
        // Given
        val tvShow = FavoriteItem(
            id = "1",
            type = FavoriteType.TV_SHOW,
            title = "Breaking Bad"
        )
        
        // When
        repository.toggleFavorite(tvShow)
        
        // Then
        repository.favorites.test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals(FavoriteType.TV_SHOW, items[0].type)
        }
    }
    
    @Test
    fun `can have multiple favorites of different types`() = runTest {
        // Given
        val movie = FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie")
        val tvShow = FavoriteItem(id = "2", type = FavoriteType.TV_SHOW, title = "Show")
        val book = FavoriteItem(id = "3", type = FavoriteType.BOOK, title = "Book")
        val game = FavoriteItem(id = "4", type = FavoriteType.GAME, title = "Game")
        
        // When
        repository.toggleFavorite(movie)
        repository.toggleFavorite(tvShow)
        repository.toggleFavorite(book)
        repository.toggleFavorite(game)
        
        // Then
        repository.favorites.test {
            val items = awaitItem()
            assertEquals(4, items.size)
            
            val types = items.map { it.type }.toSet()
            assertTrue(types.contains(FavoriteType.MOVIE))
            assertTrue(types.contains(FavoriteType.TV_SHOW))
            assertTrue(types.contains(FavoriteType.BOOK))
            assertTrue(types.contains(FavoriteType.GAME))
        }
    }
    
    @Test
    fun `syncFavorites replaces all favorites`() = runTest {
        // Given - Add initial favorites
        val initial = FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie 1")
        repository.toggleFavorite(initial)
        
        val newFavorites = listOf(
            FavoriteItem(id = "2", type = FavoriteType.MOVIE, title = "Movie 2"),
            FavoriteItem(id = "3", type = FavoriteType.TV_SHOW, title = "Show 3")
        )
        
        // When
        repository.syncFavorites(newFavorites)
        
        // Then
        repository.favorites.test {
            val items = awaitItem()
            assertEquals(2, items.size)
            
            // Old favorite should be gone
            assertFalse(items.any { it.id == "1" })
            
            // New favorites should be present
            assertTrue(items.any { it.id == "2" })
            assertTrue(items.any { it.id == "3" })
        }
    }
    
    @Test
    fun `syncFavorites with empty list clears all favorites`() = runTest {
        // Given - Add some favorites
        repository.toggleFavorite(FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie"))
        repository.toggleFavorite(FavoriteItem(id = "2", type = FavoriteType.TV_SHOW, title = "Show"))
        
        // When
        repository.syncFavorites(emptyList())
        
        // Then
        repository.favorites.test {
            val items = awaitItem()
            assertTrue(items.isEmpty(), "All favorites should be cleared")
        }
    }
    
    @Test
    fun `favorites flow emits on toggle`() = runTest {
        // Given
        val movie = FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie")
        
        // Then - Collect emissions
        repository.favorites.test {
            // Initial state
            assertEquals(0, awaitItem().size)
            
            // Toggle on
            repository.toggleFavorite(movie)
            assertEquals(1, awaitItem().size)
            
            // Toggle off
            repository.toggleFavorite(movie)
            assertEquals(0, awaitItem().size)
        }
    }
    
    @Test
    fun `favorites flow emits on sync`() = runTest {
        // Given
        val favorites = listOf(
            FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie 1"),
            FavoriteItem(id = "2", type = FavoriteType.MOVIE, title = "Movie 2")
        )
        
        // Then - Collect emissions
        repository.favorites.test {
            // Initial state
            assertEquals(0, awaitItem().size)
            
            // After sync
            repository.syncFavorites(favorites)
            assertEquals(2, awaitItem().size)
        }
    }
    
    @Test
    fun `late collector receives current state`() = runTest {
        // Given - Add favorites before collecting
        repository.toggleFavorite(FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie"))
        repository.toggleFavorite(FavoriteItem(id = "2", type = FavoriteType.TV_SHOW, title = "Show"))
        
        // When - Start collecting after favorites are added
        repository.favorites.test {
            val items = awaitItem()
            
            // Then - Should receive current state immediately
            assertEquals(2, items.size)
        }
    }
    
    @Test
    fun `can toggle same ID with different types`() = runTest {
        // Given - Same ID but different types (edge case)
        val movieWithId1 = FavoriteItem(id = "1", type = FavoriteType.MOVIE, title = "Movie 1")
        val tvShowWithId1 = FavoriteItem(id = "1", type = FavoriteType.TV_SHOW, title = "Show 1")
        
        // When
        repository.toggleFavorite(movieWithId1)
        repository.toggleFavorite(tvShowWithId1)
        
        // Then
        repository.favorites.test {
            val items = awaitItem()
            assertEquals(2, items.size, "Should have both items (different types)")
            
            val types = items.map { it.type }.toSet()
            assertTrue(types.contains(FavoriteType.MOVIE))
            assertTrue(types.contains(FavoriteType.TV_SHOW))
        }
    }
}
