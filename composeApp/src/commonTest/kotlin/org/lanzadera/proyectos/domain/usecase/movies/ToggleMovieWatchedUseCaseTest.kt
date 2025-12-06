package org.lanzadera.proyectos.domain.usecase.movies

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.fakes.FakeWatchedMoviesRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToggleMovieWatchedUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: ToggleMovieWatchedUseCase
    private lateinit var fakeRepository: FakeWatchedMoviesRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeRepository = FakeWatchedMoviesRepository()
        useCase = ToggleMovieWatchedUseCase(fakeRepository)
    }
    
    @Test
    fun `should mark movie as watched`() = runTest {
        // Given
        val movieId = "123"
        
        // When
        val result = useCase(movieId, isWatched = true)
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(fakeRepository.isMovieWatched(movieId))
    }
    
    @Test
    fun `should mark movie as unwatched`() = runTest {
        // Given
        val movieId = "123"
        fakeRepository.setWatchedMovies(setOf(movieId))
        
        // When
        val result = useCase(movieId, isWatched = false)
        
        // Then
        assertTrue(result.isSuccess)
        assertFalse(fakeRepository.isMovieWatched(movieId))
    }
    
    @Test
    fun `should toggle movie watched status multiple times`() = runTest {
        // Given
        val movieId = "123"
        
        // When - Mark as watched
        val result1 = useCase(movieId, isWatched = true)
        assertTrue(result1.isSuccess)
        assertTrue(fakeRepository.isMovieWatched(movieId))
        
        // When - Mark as unwatched
        val result2 = useCase(movieId, isWatched = false)
        assertTrue(result2.isSuccess)
        assertFalse(fakeRepository.isMovieWatched(movieId))
        
        // When - Mark as watched again
        val result3 = useCase(movieId, isWatched = true)
        assertTrue(result3.isSuccess)
        assertTrue(fakeRepository.isMovieWatched(movieId))
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        val movieId = "123"
        fakeRepository.shouldFail = true
        fakeRepository.failureException = Exception("Database error")
        
        // When
        val result = useCase(movieId, isWatched = true)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
}
