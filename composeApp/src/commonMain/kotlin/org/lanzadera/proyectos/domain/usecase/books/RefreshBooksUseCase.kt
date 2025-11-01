package org.lanzadera.proyectos.domain.usecase.books

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.repository.BooksRepository

class RefreshBooksUseCase(private val repository: BooksRepository) {
    val booksFlow: StateFlow<List<Book>> get() = repository.booksFlow

    suspend fun refreshBooks(force: Boolean = false, query: String = "") =
        repository.refreshBooks(force, query)
}

