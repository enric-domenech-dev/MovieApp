package org.lanzadera.proyectos.domain.usecase.movies

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.fakes.FakeMovieRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetMovieDetailsUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: GetMovieDetailsUseCase
    private lateinit var fakeMovieRepository: FakeMovieRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeMovieRepository = FakeMovieRepository()
        useCase = GetMovieDetailsUseCase(fakeMovieRepository)
    }
    
    @Test
    fun `should return movie details for valid movie ID`() = runTest {
        // Given
        val movie = Movie(
            id = 123, 
            title = "Test Movie",
            overview = "A great movie"
        )
        fakeMovieRepository.setMovieDetails(123, movie)
        
        // When
        val result = useCase(123)
        
        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(123, result.getOrNull()?.id)
        assertEquals("Test Movie", result.getOrNull()?.title)
    }
    
    @Test
    fun `should return null for non-existent movie`() = runTest {
        // When
        val result = useCase(999)
        
        // Then
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        fakeMovieRepository.shouldFail = true
        fakeMovieRepository.failureException = Exception("API error")
        
        // When
        val result = useCase(123)
        
        // Then
        assertTrue(result.isError)
        assertEquals("API error", result.exceptionOrNull()?.message)
    }
}
