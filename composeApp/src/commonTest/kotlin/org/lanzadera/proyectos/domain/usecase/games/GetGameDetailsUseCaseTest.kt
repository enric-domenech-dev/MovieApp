package org.lanzadera.proyectos.domain.usecase.games

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.fakes.FakeGameRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetGameDetailsUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: GetGameDetailsUseCase
    private lateinit var fakeGameRepository: FakeGameRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeGameRepository = FakeGameRepository()
        useCase = GetGameDetailsUseCase(fakeGameRepository)
    }
    
    @Test
    fun `should return game details for valid game ID`() = runTest {
        // Given
        val game = Game(id = 123, name = "Test Game", summary = "A great game")
        fakeGameRepository.setGameDetails(123, game)
        
        // When
        val result = useCase(123)
        
        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(123, result.getOrNull()?.id)
        assertEquals("Test Game", result.getOrNull()?.name)
    }
    
    @Test
    fun `should return null for non-existent game`() = runTest {
        // When
        val result = useCase(999)
        
        // Then
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        fakeGameRepository.shouldFail = true
        fakeGameRepository.failureException = Exception("API error")
        
        // When
        val result = useCase(123)
        
        // Then
        assertTrue(result.isError)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }
}
