package org.lanzadera.proyectos.ui.screens.home.tabs

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.ViewModelTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for GamesTabViewModel.
 * 
 * Tests game category flows and refresh behavior.
 * Note: RefreshGamesUseCase is optional (nullable), so tests verify behavior
 * when use case is not available.
 */
class GamesTabViewModelTest : ViewModelTest() {
    
    private lateinit var viewModel: GamesTabViewModel
    
    @BeforeTest
    override fun setup() {
        super.setup()
        // Using null use case (as it's optional in production)
        viewModel = GamesTabViewModel(refreshGamesUseCase = null)
    }
    
    @Test
    fun `initial state has empty games when use case is null`() = runTest {
        viewModel.games.test {
            assertThat(awaitItem()).isEmpty()
        }
    }
    
    @Test
    fun `all game category flows are empty when use case is null`() = runTest {
        viewModel.games.test { assertThat(awaitItem()).isEmpty() }
        viewModel.popularGames.test { assertThat(awaitItem()).isEmpty() }
        viewModel.topRatedGames.test { assertThat(awaitItem()).isEmpty() }
        viewModel.upcomingGames.test { assertThat(awaitItem()).isEmpty() }
        viewModel.trendingGames.test { assertThat(awaitItem()).isEmpty() }
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
