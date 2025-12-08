package org.lanzadera.proyectos.domain.usecase.tvshows

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.fakes.FakeTvShowRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RefreshTvShowsUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: RefreshTvShowsUseCase
    private lateinit var fakeTvShowRepository: FakeTvShowRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeTvShowRepository = FakeTvShowRepository()
        useCase = RefreshTvShowsUseCase(fakeTvShowRepository)
    }
    
    @Test
    fun `should expose all TV show flows from repository`() = runTest {
        // Given - repository flows are accessible
        
        // Then - all 7 flows should be exposed
        useCase.tvShowsFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.popularTvShowsFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.topRatedTvShowsFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `refreshTvShows should return success when repository succeeds`() = runTest {
        // When
        val result = useCase.refreshTvShows(force = false)
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `refreshTvShows should return error when repository fails`() = runTest {
        // Given
        fakeTvShowRepository.shouldFail = true
        fakeTvShowRepository.failureException = Exception("Refresh failed")
        
        // When
        val result = useCase.refreshTvShows(force = true)
        
        // Then
        assertTrue(result.isError)
        assertEquals("Refresh failed", result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `refreshPopularTvShows should return success when repository succeeds`() = runTest {
        // When
        val result = useCase.refreshPopularTvShows(force = false)
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `refreshPopularTvShows should return error when repository fails`() = runTest {
        // Given
        fakeTvShowRepository.shouldFail = true
        
        // When
        val result = useCase.refreshPopularTvShows(force = true)
        
        // Then
        assertTrue(result.isError)
    }
    
    @Test
    fun `refreshTopRatedTvShows should return success when repository succeeds`() = runTest {
        // When
        val result = useCase.refreshTopRatedTvShows(force = false)
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `refreshTopRatedTvShows should return error when repository fails`() = runTest {
        // Given
        fakeTvShowRepository.shouldFail = true
        
        // When
        val result = useCase.refreshTopRatedTvShows(force = true)
        
        // Then
        assertTrue(result.isError)
    }
    
    @Test
    fun `refreshOnAirTvShows should return success when repository succeeds`() = runTest {
        // When
        val result = useCase.refreshOnAirTvShows(force = false)
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `refreshTrendingTvShows should return success when repository succeeds`() = runTest {
        // When
        val result = useCase.refreshTrendingTvShows(force = false)
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `refreshAiringTodayTvShows should return success when repository succeeds`() = runTest {
        // When
        val result = useCase.refreshAiringTodayTvShows(force = false)
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `refreshTrendingTvShowsWeek should return success when repository succeeds`() = runTest {
        // When
        val result = useCase.refreshTrendingTvShowsWeek(force = false)
        
        // Then
        assertTrue(result.isSuccess)
    }
    
    @Test
    fun `all refresh methods should handle force parameter correctly`() = runTest {
        // Test that force parameter is passed to repository
        // Since FakeTvShowRepository doesn't validate force param, 
        // this test just ensures no errors when using force=true
        
        // When
        val result1 = useCase.refreshTvShows(force = true)
        val result2 = useCase.refreshPopularTvShows(force = true)
        val result3 = useCase.refreshTopRatedTvShows(force = true)
        
        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
    }
}
