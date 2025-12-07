package org.lanzadera.proyectos.ui.screens.home.tabs

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.ViewModelTest
import org.lanzadera.proyectos.domain.usecase.load_initial_data.GetInitialDataUseCase
import org.lanzadera.proyectos.fakes.FakeLoadInitialDataRepository
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for FilmsTabViewModel.
 * 
 * Tests that the ViewModel correctly exposes movie flows from GetInitialDataUseCase
 * and maps them to UI models.
 */
class FilmsTabViewModelTest : ViewModelTest() {
    
    private lateinit var viewModel: FilmsTabViewModel
    private lateinit var fakeRepository: FakeLoadInitialDataRepository
    
    @BeforeTest
    override fun setup() {
        super.setup()
        fakeRepository = FakeLoadInitialDataRepository()
        val useCase = GetInitialDataUseCase(fakeRepository)
        viewModel = FilmsTabViewModel(useCase)
    }
    
    @Test
    fun `initial state has empty movies`() = runTest {
        viewModel.movies.test {
            val initial = awaitItem()
            assertThat(initial).isEmpty()
        }
    }
    
    @Test
    fun `movies flow emits UI models when repository updates`() = runTest {
        viewModel.movies.test {
            // Initial empty state
            assertThat(awaitItem()).isEmpty()
            
            // Add test movies to fake repository
            fakeRepository.addTestMovies(3)
            testDispatcher.scheduler.advanceUntilIdle()
            
            val movies = awaitItem()
            assertThat(movies).hasSize(3)
            assertThat(movies[0].title).isEqualTo("Test Movie 0")
        }
    }
    
    @Test
    fun `popular movies flow emits UI models`() = runTest {
        viewModel.popular.test {
            // Initial empty state
            assertThat(awaitItem()).isEmpty()
            
            fakeRepository.addTestPopularMovies(2)
            testDispatcher.scheduler.advanceUntilIdle()
            
            val popular = awaitItem()
            assertThat(popular).hasSize(2)
            assertThat(popular[0].title).isEqualTo("Popular Movie 0")
        }
    }
    
    @Test
    fun `all movie category flows are exposed`() = runTest {
        // Verify all 9 flows exist and are initially empty
        viewModel.movies.test { assertThat(awaitItem()).isEmpty() }
        viewModel.trendingWeek.test { assertThat(awaitItem()).isEmpty() }
        viewModel.trendingDay.test { assertThat(awaitItem()).isEmpty() }
        viewModel.popular.test { assertThat(awaitItem()).isEmpty() }
        viewModel.topRated.test { assertThat(awaitItem()).isEmpty() }
        viewModel.upcoming.test { assertThat(awaitItem()).isEmpty() }
        viewModel.discover.test { assertThat(awaitItem()).isEmpty() }
        viewModel.hero.test { assertThat(awaitItem()).isEmpty() }
        viewModel.inCinemasToday.test { assertThat(awaitItem()).isEmpty() }
    }
}
