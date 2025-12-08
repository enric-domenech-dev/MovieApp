package org.lanzadera.proyectos.domain.usecase.load_initial_data

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.UseCaseTest
import org.lanzadera.proyectos.fakes.FakeLoadInitialDataRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetInitialDataUseCaseTest : UseCaseTest() {
    
    private lateinit var useCase: GetInitialDataUseCase
    private lateinit var fakeLoadInitialDataRepository: FakeLoadInitialDataRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeLoadInitialDataRepository = FakeLoadInitialDataRepository()
        useCase = GetInitialDataUseCase(fakeLoadInitialDataRepository)
    }
    
    @Test
    fun `should expose all movie flows from repository`() = runTest {
        // Given - repository has 9 movie flows
        
        // Then - all 9 flows should be exposed
        useCase.moviesFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.trendingMoviesFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.trendingMoviesDailyFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.popularMoviesFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.topRatedMoviesFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.upcomingMoviesFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.discoverMoviesFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.heroMoviesFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.inCinemasTodayFlow.test {
            assertEquals(emptyList(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `moviesFlow should emit movies from repository`() = runTest {
        // Given
        fakeLoadInitialDataRepository.addTestMovies(3)
        
        // When
        useCase.moviesFlow.test {
            // Then
            val movies = awaitItem()
            assertEquals(3, movies.size)
            assertEquals("Test Movie 0", movies[0].title)
            assertEquals("Test Movie 1", movies[1].title)
            assertEquals("Test Movie 2", movies[2].title)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `popularMoviesFlow should emit popular movies from repository`() = runTest {
        // Given
        fakeLoadInitialDataRepository.addTestPopularMovies(2)
        
        // When
        useCase.popularMoviesFlow.test {
            // Then
            val movies = awaitItem()
            assertEquals(2, movies.size)
            assertEquals("Popular Movie 0", movies[0].title)
            assertEquals("Popular Movie 1", movies[1].title)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `topRatedMoviesFlow should emit top rated movies from repository`() = runTest {
        // Given
        fakeLoadInitialDataRepository.addTestTopRatedMovies(5)
        
        // When
        useCase.topRatedMoviesFlow.test {
            // Then
            val movies = awaitItem()
            assertEquals(5, movies.size)
            assertEquals("Top Rated Movie 0", movies[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `all flows should be read-only and independent`() = runTest {
        // Given
        fakeLoadInitialDataRepository.addTestMovies(2)
        fakeLoadInitialDataRepository.addTestPopularMovies(3)
        
        // When/Then - Each flow should have independent data
        useCase.moviesFlow.test {
            assertEquals(2, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.popularMoviesFlow.test {
            assertEquals(3, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
        
        useCase.trendingMoviesFlow.test {
            assertEquals(0, awaitItem().size) // Not populated
            cancelAndIgnoreRemainingEvents()
        }
    }
}
