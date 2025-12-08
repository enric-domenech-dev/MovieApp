package org.lanzadera.proyectos.domain.usecase.search

import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.fakes.FakeSearchRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchTvShowsUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: SearchTvShowsUseCase
    private lateinit var fakeSearchRepository: FakeSearchRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeSearchRepository = FakeSearchRepository()
        useCase = SearchTvShowsUseCase(fakeSearchRepository)
    }
    
    @Test
    fun `should return empty list for empty query`() = runTest {
        // When
        val result = useCase("")
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
    
    @Test
    fun `should return empty list for whitespace query`() = runTest {
        // When
        val result = useCase("   ")
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
    
    @Test
    fun `should return TV shows for valid query`() = runTest {
        // Given
        val tvShows = listOf(
            TvShow(id = 1, name = "Breaking Bad"),
            TvShow(id = 2, name = "Better Call Saul")
        )
        fakeSearchRepository.setTvShowResults("breaking", tvShows)
        
        // When
        val result = useCase("breaking")
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Breaking Bad", result.getOrNull()?.get(0)?.name)
    }
    
    @Test
    fun `should return empty list for query with no results`() = runTest {
        // Given
        fakeSearchRepository.setTvShowResults("nonexistent", emptyList())
        
        // When
        val result = useCase("nonexistent")
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
    
    @Test
    fun `should return error when repository fails`() = runTest {
        // Given
        fakeSearchRepository.shouldFail = true
        fakeSearchRepository.failureException = Exception("Network error")
        
        // When
        val result = useCase("breaking")
        
        // Then
        assertTrue(result.isError)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}
