package org.lanzadera.proyectos.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.lanzadera.proyectos.domain.models.book.Book

/**
 * Repository for managing book data from Google Books API.
 *
 * Provides access to various book categories with local caching.
 * All data is exposed via StateFlows for reactive UI updates.
 */
interface BooksRepository {
    /** Flow of general books */
    val booksFlow: StateFlow<List<Book>>
    
    /** Flow of fiction books */
    val fictionBooksFlow: StateFlow<List<Book>>
    
    /** Flow of science books */
    val scienceBooksFlow: StateFlow<List<Book>>
    
    /** Flow of history books */
    val historyBooksFlow: StateFlow<List<Book>>
    
    /** Flow of biography books */
    val biographyBooksFlow: StateFlow<List<Book>>
    
    /** Flow of business books */
    val businessBooksFlow: StateFlow<List<Book>>
    
    /** Flow of technology books */
    val technologyBooksFlow: StateFlow<List<Book>>
    
    /** Flow of self-help books */
    val selfHelpBooksFlow: StateFlow<List<Book>>
    
    /** Flow of recent books */
    val recentBooksFlow: StateFlow<List<Book>>

    /**
     * Refreshes general books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     * @param query Custom search query
     */
    suspend fun refreshBooks(force: Boolean = false, query: String = "")
    
    /**
     * Refreshes fiction books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshFictionBooks(force: Boolean = false)
    
    /**
     * Refreshes science books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshScienceBooks(force: Boolean = false)
    
    /**
     * Refreshes history books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshHistoryBooks(force: Boolean = false)
    
    /**
     * Refreshes biography books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshBiographyBooks(force: Boolean = false)
    
    /**
     * Refreshes business books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshBusinessBooks(force: Boolean = false)
    
    /**
     * Refreshes technology books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshTechnologyBooks(force: Boolean = false)
    
    /**
     * Refreshes self-help books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshSelfHelpBooks(force: Boolean = false)
    
    /**
     * Refreshes recent books from Google Books API.
     * @param force If true, bypasses cache and forces a fresh fetch
     */
    suspend fun refreshRecentBooks(force: Boolean = false)
}

