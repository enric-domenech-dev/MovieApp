package org.lanzadera.proyectos.domain.usecase.games

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.fakes.FakeGameRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RefreshGamesUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: RefreshGamesUseCase
    private lateinit var fakeGameRepository: FakeGameRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeGameRepository = FakeGameRepository()
        useCase = RefreshGamesUseCase(fakeGameRepository)
    }
    
    @Test
    fun `should refresh all game categories successfully`() = runTest {
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `should populate all game flows after refresh`() = runTest {
        // When
        useCase()
        
        // Then - Check all flows have data
        useCase.gamesFlow.test {
            assertEquals(1, awaitItem().size)
        }
        useCase.popularGamesFlow.test {
            assertEquals(1, awaitItem().size)
        }
        useCase.topRatedGamesFlow.test {
            assertEquals(1, awaitItem().size)
        }
        useCase.upcomingGamesFlow.test {
            assertEquals(1, awaitItem().size)
        }
        useCase.trendingGamesFlow.test {
            assertEquals(1, awaitItem().size)
        }
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        fakeGameRepository.shouldFail = true
        fakeGameRepository.failureException = Exception("Network error")
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isError)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
