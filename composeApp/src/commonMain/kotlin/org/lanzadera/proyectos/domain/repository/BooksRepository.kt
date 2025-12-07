package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.book.Book

interface BooksRepository {
    val booksFlow: StateFlow<List<Book>>
    val fictionBooksFlow: StateFlow<List<Book>>
    val scienceBooksFlow: StateFlow<List<Book>>
    val historyBooksFlow: StateFlow<List<Book>>
    val biographyBooksFlow: StateFlow<List<Book>>
    val businessBooksFlow: StateFlow<List<Book>>
    val technologyBooksFlow: StateFlow<List<Book>>
    val selfHelpBooksFlow: StateFlow<List<Book>>
    val recentBooksFlow: StateFlow<List<Book>>

    suspend fun refreshBooks(force: Boolean = false, query: String = "")
    suspend fun refreshFictionBooks(force: Boolean = false)
    suspend fun refreshScienceBooks(force: Boolean = false)
    suspend fun refreshHistoryBooks(force: Boolean = false)
    suspend fun refreshBiographyBooks(force: Boolean = false)
    suspend fun refreshBusinessBooks(force: Boolean = false)
    suspend fun refreshTechnologyBooks(force: Boolean = false)
    suspend fun refreshSelfHelpBooks(force: Boolean = false)
    suspend fun refreshRecentBooks(force: Boolean = false)
}

