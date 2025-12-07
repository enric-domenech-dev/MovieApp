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
 * Base class for Use Case tests.
 * 
 * Provides common setup for testing use cases with suspend functions.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class UseCaseTest {
    
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
