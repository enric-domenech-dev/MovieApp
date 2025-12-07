package org.lanzadera.proyectos.ui.screens.home.tabs

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.ViewModelTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for SeriesTabViewModel.
 * 
 * Tests TV show category flows and refresh behavior.
 * Note: RefreshTvShowsUseCase is optional (nullable), so tests verify behavior
 * when use case is not available.
 */
class SeriesTabViewModelTest : ViewModelTest() {
    
    private lateinit var viewModel: SeriesTabViewModel
    
    @BeforeTest
    override fun setup() {
        super.setup()
        // Using null use case (as it's optional in production)
        viewModel = SeriesTabViewModel(refreshTvShowsUseCase = null)
    }
    
    @Test
    fun `initial state has empty tvShows when use case is null`() = runTest {
        viewModel.tvShows.test {
            assertThat(awaitItem()).isEmpty()
        }
    }
    
    @Test
    fun `all TV show category flows are empty when use case is null`() = runTest {
        viewModel.tvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.popularTvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.topRatedTvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.onAirTvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.trendingTvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.airingTodayTvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.trendingTvShowsWeek.test { assertThat(awaitItem()).isEmpty() }
    }
    
    @Test
    fun `derived flows are empty when base flows are empty`() = runTest {
        viewModel.airingTodayAndTrendingTvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.recommendedTvShows.test { assertThat(awaitItem()).isEmpty() }
        viewModel.upcomingTvShows.test { assertThat(awaitItem()).isEmpty() }
    }
    
    @Test
    fun `isRefreshing is false initially`() = runTest {
        viewModel.isRefreshing.test {
            assertThat(awaitItem()).isFalse()
        }
    }
    
    @Test
    fun `error is null initially`() = runTest {
        viewModel.error.test {
            assertThat(awaitItem()).isNull()
        }
    }
}
