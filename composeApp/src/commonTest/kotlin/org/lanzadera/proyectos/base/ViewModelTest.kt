package org.lanzadera.proyectos.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Base class for ViewModel tests.
 * 
 * Provides common setup for testing ViewModels with coroutines:
 * - Sets up TestDispatcher for viewModelScope
 * - Provides test dispatcher for Flow collection
 * - Cleans up after tests
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class ViewModelTest {
    
    protected lateinit var testDispatcher: TestDispatcher
    
    @BeforeTest
    open fun setup() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }
    
    @AfterTest
    open fun tearDown() {
        Dispatchers.resetMain()
    }
}
