package org.lanzadera.proyectos.ui.screens.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.ViewModelTest
import org.lanzadera.proyectos.ui.screens.home.HomeViewModel.HomeTab
import kotlin.test.Test

/**
 * Tests for HomeViewModel.
 * 
 * Tests tab selection and navigation logic.
 */
class HomeViewModelTest : ViewModelTest() {
    
    private lateinit var viewModel: HomeViewModel
    
    override fun setup() {
        super.setup()
        viewModel = HomeViewModel()
    }
    
    @Test
    fun `initial state is FAVORITES tab`() = runTest {
        viewModel.selectedTab.test {
            assertThat(awaitItem()).isEqualTo(HomeTab.FAVORITES)
        }
    }
    
    @Test
    fun `selectTab updates selectedTab state`() = runTest {
        viewModel.selectedTab.test {
            // Initial state
            assertThat(awaitItem()).isEqualTo(HomeTab.FAVORITES)
            
            // Select BOOKS (index 1)
            viewModel.selectTab(1)
            assertThat(awaitItem()).isEqualTo(HomeTab.BOOKS)
            
            // Select FILMS (index 2)
            viewModel.selectTab(2)
            assertThat(awaitItem()).isEqualTo(HomeTab.FILMS)
            
            // Select SERIES (index 3)
            viewModel.selectTab(3)
            assertThat(awaitItem()).isEqualTo(HomeTab.SERIES)
            
            // Select GAMES (index 4)
            viewModel.selectTab(4)
            assertThat(awaitItem()).isEqualTo(HomeTab.GAMES)
            
            // Select FAVORITES again (index 0)
            viewModel.selectTab(0)
            assertThat(awaitItem()).isEqualTo(HomeTab.FAVORITES)
        }
    }
    
    @Test
    fun `getTabIndex returns correct index for current tab`() {
        // Initially FAVORITES
        assertThat(viewModel.getTabIndex()).isEqualTo(0)
        
        // Select BOOKS
        viewModel.selectTab(1)
        assertThat(viewModel.getTabIndex()).isEqualTo(1)
        
        // Select FILMS
        viewModel.selectTab(2)
        assertThat(viewModel.getTabIndex()).isEqualTo(2)
        
        // Select SERIES
        viewModel.selectTab(3)
        assertThat(viewModel.getTabIndex()).isEqualTo(3)
        
        // Select GAMES
        viewModel.selectTab(4)
        assertThat(viewModel.getTabIndex()).isEqualTo(4)
    }
}
