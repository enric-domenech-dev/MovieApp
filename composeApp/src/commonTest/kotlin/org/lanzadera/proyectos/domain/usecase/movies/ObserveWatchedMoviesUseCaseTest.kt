package org.lanzadera.proyectos.domain.usecase.movies

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.WatchedMovie
import org.lanzadera.proyectos.fakes.FakeWatchedMoviesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObserveWatchedMoviesUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ObserveWatchedMoviesUseCase
    private lateinit var fakeWatchedMoviesRepository: FakeWatchedMoviesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeWatchedMoviesRepository = FakeWatchedMoviesRepository()
        useCase = ObserveWatchedMoviesUseCase(fakeWatchedMoviesRepository)
    }
    
    @Test
    fun `should emit empty list initially`() = runTest {
        // When
        useCase().test {
            // Then
            val movies = awaitItem()
            assertTrue(movies.isEmpty())
        }
    }
    
    @Test
    fun `should emit watched movies when added`() = runTest {
        // When
        useCase().test {
            // Initially empty
            assertTrue(awaitItem().isEmpty())
            
            // Add watched movie
            fakeWatchedMoviesRepository.toggleMovieWatched("123", true)
            
            // Then - Should emit updated list
            val movies = awaitItem()
            assertEquals(1, movies.size)
            assertEquals("123", movies[0].movieId)
        }
    }
    
    @Test
    fun `should emit multiple watched movies`() = runTest {
        // When
        useCase().test {
            // Initially empty
            assertTrue(awaitItem().isEmpty())
            
            // Add movies
            fakeWatchedMoviesRepository.toggleMovieWatched("123", true)
            assertEquals(1, awaitItem().size)
            
            fakeWatchedMoviesRepository.toggleMovieWatched("456", true)
            
            // Then
            val movies = awaitItem()
            assertEquals(2, movies.size)
        }
    }
    
    @Test
    fun `should emit updated list when movie unmarked`() = runTest {
        // Given - Two watched movies
        fakeWatchedMoviesRepository.toggleMovieWatched("123", true)
        fakeWatchedMoviesRepository.toggleMovieWatched("456", true)
        
        // When
        useCase().test {
            // Initial state with 2 movies
            assertEquals(2, awaitItem().size)
            
            // Unmark one
            fakeWatchedMoviesRepository.toggleMovieWatched("123", false)
            
            // Then - Should emit updated list with 1 item
            val movies = awaitItem()
            assertEquals(1, movies.size)
            assertEquals("456", movies[0].movieId)
        }
    }
}
