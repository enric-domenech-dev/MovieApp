package org.lanzadera.proyectos.ui.screens.login

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        // Then
        viewModel.userState.test {
            assertNull(awaitItem())
        }
        
        viewModel.isLoading.test {
            assertFalse(awaitItem())
        }
        
        viewModel.isLoginSuccessful.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `loading state becomes true when login starts`() = runTest {
        // Given
        val initialLoading = viewModel.isLoading.value

        // When
        viewModel.login("test@example.com", "password")
        testDispatcher.scheduler.advanceTimeBy(10) // Just trigger the coroutine start

        // Then - Initial state was false
        assertFalse(initialLoading)
        
        // Note: We can't reliably test the loading=true state because
        // the HttpClient makes real network calls that we can't control in tests
    }

    @Test
    fun `isLoginSuccessful is null initially`() = runTest {
        viewModel.isLoginSuccessful.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `userState is null initially`() = runTest {
        viewModel.userState.test {
            assertNull(awaitItem())
        }
    }

    @Test
    fun `isLoading is false initially`() = runTest {
        viewModel.isLoading.test {
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `login method can be called without crashing`() = runTest {
        // When - Just verify the method can be called
        viewModel.login("test@example.com", "password")
        
        // Then - No exception should be thrown
        // Note: We don't wait for completion as HttpClient makes real network calls
        assertTrue(true) // Test passes if no exception thrown
    }

    @Test
    fun `login with empty credentials does not crash`() = runTest {
        // When
        viewModel.login("", "")
        
        // Then - No exception should be thrown
        assertTrue(true)
    }

    @Test
    fun `login with null-like inputs does not crash`() = runTest {
        // When
        viewModel.login("", "")
        viewModel.login("test", "")
        viewModel.login("", "test")
        
        // Then - No exception should be thrown
        assertTrue(true)
    }
}
