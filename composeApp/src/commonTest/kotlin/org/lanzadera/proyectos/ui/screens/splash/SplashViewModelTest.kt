package org.lanzadera.proyectos.ui.screens.splash

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.lanzadera.proyectos.fakes.FakeLoadInitialDataRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private lateinit var viewModel: SplashViewModel
    private lateinit var fakeLoadInitialDataRepository: FakeLoadInitialDataRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeLoadInitialDataRepository = FakeLoadInitialDataRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial loading state is false`() = runTest {
        // Given & When
        viewModel = SplashViewModel(fakeLoadInitialDataRepository)

        // Then
        viewModel.isLoadingComplete.test {
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `loading completes after init block finishes`() = runTest {
        // Given
        viewModel = SplashViewModel(fakeLoadInitialDataRepository)

        // When - Wait for init block to complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.isLoadingComplete.test {
            assertTrue(awaitItem())
        }
    }

    @Test
    fun `loading state changes from false to true`() = runTest {
        // Given
        viewModel = SplashViewModel(fakeLoadInitialDataRepository)

        // Then
        viewModel.isLoadingComplete.test {
            // Initial state
            assertFalse(awaitItem())
            
            // Advance time to complete loading
            testDispatcher.scheduler.advanceUntilIdle()
            
            // Final state
            assertTrue(awaitItem())
        }
    }

    @Test
    fun `loading completes even when repository throws exception`() = runTest {
        // Given
        val failingRepository = FailingLoadInitialDataRepository()
        
        // When
        viewModel = SplashViewModel(failingRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should complete loading despite error
        viewModel.isLoadingComplete.test {
            assertTrue(awaitItem())
        }
    }

    // Helper class to simulate repository failure
    private class FailingLoadInitialDataRepository : FakeLoadInitialDataRepository() {
        override suspend fun refreshMovies(force: Boolean) {
            throw Exception("Test failure")
        }
    }
}
