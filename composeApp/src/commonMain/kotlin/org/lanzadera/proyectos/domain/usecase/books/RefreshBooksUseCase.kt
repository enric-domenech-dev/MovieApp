package org.lanzadera.proyectos.domain.usecase.books

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.repository.BooksRepository
import org.lanzadera.proyectos.utils.Logger

class RefreshBooksUseCase(private val repository: BooksRepository) {
    val booksFlow: StateFlow<List<Book>> get() = repository.booksFlow
    val fictionBooksFlow: StateFlow<List<Book>> get() = repository.fictionBooksFlow
    val scienceBooksFlow: StateFlow<List<Book>> get() = repository.scienceBooksFlow
    val historyBooksFlow: StateFlow<List<Book>> get() = repository.historyBooksFlow
    val biographyBooksFlow: StateFlow<List<Book>> get() = repository.biographyBooksFlow
    val businessBooksFlow: StateFlow<List<Book>> get() = repository.businessBooksFlow
    val technologyBooksFlow: StateFlow<List<Book>> get() = repository.technologyBooksFlow
    val selfHelpBooksFlow: StateFlow<List<Book>> get() = repository.selfHelpBooksFlow
    val recentBooksFlow: StateFlow<List<Book>> get() = repository.recentBooksFlow

    suspend fun refreshBooks(force: Boolean = false, query: String = ""): Result<Unit> {
        return try {
            repository.refreshBooks(force, query)
            Logger.d("Books refreshed, force=$force, query=$query", tag = "RefreshBooksUseCase")
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e, "No se pudieron actualizar los libros")
        }
    }

    suspend fun refreshFictionBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshFictionBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing fiction books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshScienceBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshScienceBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing science books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshHistoryBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshHistoryBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing history books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshBiographyBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshBiographyBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing biography books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshBusinessBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshBusinessBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing business books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshTechnologyBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshTechnologyBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing technology books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshSelfHelpBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshSelfHelpBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing self help books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }

    suspend fun refreshRecentBooks(force: Boolean = false): Result<Unit> {
        return try {
            repository.refreshRecentBooks(force)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.e("Error refreshing recent books", tag = "RefreshBooksUseCase", throwable = e)
            Result.Error(e)
        }
    }
}

