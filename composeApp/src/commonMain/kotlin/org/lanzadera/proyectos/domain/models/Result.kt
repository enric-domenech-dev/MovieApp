package org.lanzadera.proyectos.domain.models

/**
 * A generic wrapper for handling results of operations that can fail.
 * 
 * This sealed class represents the outcome of an operation, providing
 * type-safe error handling without exceptions.
 * 
 * @param T The type of data returned on success
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data.
     * 
     * @param data The result data
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents a failed operation with error information.
     * 
     * @param exception The exception that caused the failure
     * @param message Optional user-friendly error message
     */
    data class Error(
        val exception: Throwable,
        val message: String? = exception.message
    ) : Result<Nothing>()
    
    /**
     * Represents an operation in progress.
     */
    data object Loading : Result<Nothing>()
    
    /**
     * Returns true if this is a Success result.
     */
    val isSuccess: Boolean
        get() = this is Success
    
    /**
     * Returns true if this is an Error result.
     */
    val isError: Boolean
        get() = this is Error
    
    /**
     * Returns true if this is a Loading result.
     */
    val isLoading: Boolean
        get() = this is Loading
    
    /**
     * Returns the data if this is a Success, or null otherwise.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    /**
     * Returns the exception if this is an Error, or null otherwise.
     */
    fun exceptionOrNull(): Throwable? = when (this) {
        is Error -> exception
        else -> null
    }
}

/**
 * Extension function to execute a block and wrap the result.
 * 
 * Usage:
 * ```
 * val result = resultOf {
 *     someOperationThatMightFail()
 * }
 * ```
 */
inline fun <T> resultOf(block: () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
