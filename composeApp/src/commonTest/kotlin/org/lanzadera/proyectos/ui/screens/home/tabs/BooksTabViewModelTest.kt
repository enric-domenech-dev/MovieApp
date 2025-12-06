package org.lanzadera.proyectos.ui.screens.home.tabs

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.lanzadera.proyectos.base.ViewModelTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for BooksTabViewModel.
 * 
 * Tests book category flows and refresh behavior.
 * Note: RefreshBooksUseCase is optional (nullable), so tests verify behavior
 * when use case is not available.
 */
class BooksTabViewModelTest : ViewModelTest() {
    
    private lateinit var viewModel: BooksTabViewModel
    
    @BeforeTest
    override fun setup() {
        super.setup()
        // Using null use case (as it's optional in production)
        viewModel = BooksTabViewModel(refreshBooksUseCase = null)
    }
    
    @Test
    fun `initial state has empty books when use case is null`() = runTest {
        viewModel.books.test {
            assertThat(awaitItem()).isEmpty()
        }
    }
    
    @Test
    fun `all book category flows are empty when use case is null`() = runTest {
        viewModel.books.test { assertThat(awaitItem()).isEmpty() }
        viewModel.fictionBooks.test { assertThat(awaitItem()).isEmpty() }
        viewModel.scienceBooks.test { assertThat(awaitItem()).isEmpty() }
        viewModel.historyBooks.test { assertThat(awaitItem()).isEmpty() }
        viewModel.biographyBooks.test { assertThat(awaitItem()).isEmpty() }
        viewModel.businessBooks.test { assertThat(awaitItem()).isEmpty() }
        viewModel.technologyBooks.test { assertThat(awaitItem()).isEmpty() }
        viewModel.selfHelpBooks.test { assertThat(awaitItem()).isEmpty() }
        viewModel.recentBooks.test { assertThat(awaitItem()).isEmpty() }
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
