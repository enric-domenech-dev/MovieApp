package org.lanzadera.proyectos.domain.usecase.books

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.repository.BooksRepository

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

    suspend fun refreshBooks(force: Boolean = false, query: String = "") =
        repository.refreshBooks(force, query)

    suspend fun refreshFictionBooks(force: Boolean = false) =
        repository.refreshFictionBooks(force)

    suspend fun refreshScienceBooks(force: Boolean = false) =
        repository.refreshScienceBooks(force)

    suspend fun refreshHistoryBooks(force: Boolean = false) =
        repository.refreshHistoryBooks(force)

    suspend fun refreshBiographyBooks(force: Boolean = false) =
        repository.refreshBiographyBooks(force)

    suspend fun refreshBusinessBooks(force: Boolean = false) =
        repository.refreshBusinessBooks(force)

    suspend fun refreshTechnologyBooks(force: Boolean = false) =
        repository.refreshTechnologyBooks(force)

    suspend fun refreshSelfHelpBooks(force: Boolean = false) =
        repository.refreshSelfHelpBooks(force)

    suspend fun refreshRecentBooks(force: Boolean = false) =
        repository.refreshRecentBooks(force)
}

