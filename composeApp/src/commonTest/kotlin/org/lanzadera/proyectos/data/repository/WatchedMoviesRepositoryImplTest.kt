package org.lanzadera.proyectos.data.repository

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.domain.models.WatchedMovie
import org.lanzadera.proyectos.fakes.FakeWatchedMoviesDataSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WatchedMoviesRepositoryImplTest {
    
    private lateinit var dataSource: FakeWatchedMoviesDataSource
    private lateinit var repository: WatchedMoviesRepositoryImpl
    
    @BeforeTest
    fun setup() {
        dataSource = FakeWatchedMoviesDataSource()
        repository = WatchedMoviesRepositoryImpl(dataSource)
    }
    
    @Test
    fun `observeAllWatchedMovies emits empty list initially`() = runTest {
        repository.observeAllWatchedMovies().test {
            val movies = awaitItem()
            assertTrue(movies.isEmpty())
        }
    }
    
    @Test
    fun `observeAllWatchedMovies emits watched movies after marking as watched`() = runTest {
        repository.observeAllWatchedMovies().test {
            // Initial state
            assertEquals(0, awaitItem().size)
            
            // Mark movie as watched
            repository.toggleMovieWatched("123", true)
            
            // Should emit updated list
            val movies = awaitItem()
            assertEquals(1, movies.size)
            assertEquals("123", movies[0].movieId)
        }
    }
    
    @Test
    fun `toggleMovieWatched marks movie as watched when isWatched is true`() = runTest {
        repository.toggleMovieWatched("456", true)
        
        val isWatched = repository.isMovieWatched("456")
        assertTrue(isWatched)
    }
    
    @Test
    fun `toggleMovieWatched marks movie as unwatched when isWatched is false`() = runTest {
        // First mark as watched
        repository.toggleMovieWatched("789", true)
        assertTrue(repository.isMovieWatched("789"))
        
        // Then mark as unwatched
        repository.toggleMovieWatched("789", false)
        assertFalse(repository.isMovieWatched("789"))
    }
    
    @Test
    fun `isMovieWatched returns false for unwatched movie`() = runTest {
        val isWatched = repository.isMovieWatched("999")
        assertFalse(isWatched)
    }
    
    @Test
    fun `isMovieWatched returns true for watched movie`() = runTest {
        repository.toggleMovieWatched("111", true)
        
        val isWatched = repository.isMovieWatched("111")
        assertTrue(isWatched)
    }
    
    @Test
    fun `multiple movies can be tracked simultaneously`() = runTest {
        repository.observeAllWatchedMovies().test {
            // Initial state
            assertEquals(0, awaitItem().size)
            
            // Mark multiple movies as watched
            repository.toggleMovieWatched("1", true)
            assertEquals(1, awaitItem().size)
            
            repository.toggleMovieWatched("2", true)
            assertEquals(2, awaitItem().size)
            
            repository.toggleMovieWatched("3", true)
            val movies = awaitItem()
            assertEquals(3, movies.size)
            
            // Verify all are tracked
            assertTrue(repository.isMovieWatched("1"))
            assertTrue(repository.isMovieWatched("2"))
            assertTrue(repository.isMovieWatched("3"))
        }
    }
    
    @Test
    fun `toggling same movie multiple times works correctly`() = runTest {
        repository.observeAllWatchedMovies().test {
            assertEquals(0, awaitItem().size)
            
            // Mark as watched
            repository.toggleMovieWatched("555", true)
            assertEquals(1, awaitItem().size)
            
            // Mark as unwatched
            repository.toggleMovieWatched("555", false)
            assertEquals(0, awaitItem().size)
            
            // Mark as watched again
            repository.toggleMovieWatched("555", true)
            val movies = awaitItem()
            assertEquals(1, movies.size)
            assertEquals("555", movies[0].movieId)
        }
    }
    
    @Test
    fun `observeAllWatchedMovies reflects current state when collected late`() = runTest {
        // Mark movies as watched before observing
        repository.toggleMovieWatched("100", true)
        repository.toggleMovieWatched("200", true)
        
        // Start observing - should get current state
        repository.observeAllWatchedMovies().test {
            val movies = awaitItem()
            assertEquals(2, movies.size)
            assertTrue(movies.any { it.movieId == "100" })
            assertTrue(movies.any { it.movieId == "200" })
        }
    }
    
    @Test
    fun `unwatching non-existent movie does not cause error`() = runTest {
        // Should not crash or throw
        repository.toggleMovieWatched("non_existent", false)
        
        val isWatched = repository.isMovieWatched("non_existent")
        assertFalse(isWatched)
    }
}
