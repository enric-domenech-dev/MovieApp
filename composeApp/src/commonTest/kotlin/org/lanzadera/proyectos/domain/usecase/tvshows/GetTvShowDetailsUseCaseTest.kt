package org.lanzadera.proyectos.domain.usecase.tvshows

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.fakes.FakeTvShowRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetTvShowDetailsUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: GetTvShowDetailsUseCase
    private lateinit var fakeTvShowRepository: FakeTvShowRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeTvShowRepository = FakeTvShowRepository()
        useCase = GetTvShowDetailsUseCase(fakeTvShowRepository)
    }
    
    @Test
    fun `should return TV show details for valid show ID`() = runTest {
        // Given
        val tvShow = TvShow(
            id = 456,
            name = "Test Series",
            overview = "A great series"
        )
        fakeTvShowRepository.setTvShowDetails(456, tvShow)
        
        // When
        val result = useCase(456)
        
        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(456, result.getOrNull()?.id)
        assertEquals("Test Series", result.getOrNull()?.name)
    }
    
    @Test
    fun `should return null for non-existent TV show`() = runTest {
        // When
        val result = useCase(999)
        
        // Then
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        fakeTvShowRepository.shouldFail = true
        fakeTvShowRepository.failureException = Exception("Network error")
        
        // When
        val result = useCase(456)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
