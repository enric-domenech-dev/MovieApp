package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.book.Book

interface BooksRepository {
    val booksFlow: StateFlow<List<Book>>

    suspend fun refreshBooks(force: Boolean = false, query: String = "")
}

