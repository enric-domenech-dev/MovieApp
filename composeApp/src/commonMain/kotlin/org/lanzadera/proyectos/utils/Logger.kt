package org.lanzadera.proyectos.utils

import io.github.aakira.napier.Napier

/**
 * Logging utility wrapper around Napier.
 * 
 * Provides consistent logging interface across all platforms.
 * In debug builds, logs are printed. In release builds, logs are suppressed.
 */
object Logger {
    
    /**
     * Debug log - for detailed debugging information
     */
    fun d(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.d(message = message, throwable = throwable, tag = tag ?: "MovieApp")
    }
    
    /**
     * Info log - for general information
     */
    fun i(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.i(message = message, throwable = throwable, tag = tag ?: "MovieApp")
    }
    
    /**
     * Warning log - for potentially harmful situations
     */
    fun w(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.w(message = message, throwable = throwable, tag = tag ?: "MovieApp")
    }
    
    /**
     * Error log - for error events
     */
    fun e(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.e(message = message, throwable = throwable, tag = tag ?: "MovieApp")
    }
    
    /**
     * Verbose log - for very detailed debugging (lowest priority)
     */
    fun v(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.v(message = message, throwable = throwable, tag = tag ?: "MovieApp")
    }
}
