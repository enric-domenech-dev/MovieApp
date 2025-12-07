package org.lanzadera.proyectos.utils

import kotlin.test.Test
import kotlin.test.assertNotNull

class LoggerTest {
    
    @Test
    fun `Logger object exists`() {
        assertNotNull(Logger)
    }
    
    @Test
    fun `Logger methods can be called without crashing`() {
        // These should not crash
        Logger.d("Debug message")
        Logger.i("Info message")
        Logger.w("Warning message")
        Logger.e("Error message")
        Logger.v("Verbose message")
    }
    
    @Test
    fun `Logger accepts custom tags`() {
        Logger.d("Test message", tag = "TestTag")
        Logger.e("Error message", tag = "ErrorTag", throwable = Exception("Test exception"))
    }
}
