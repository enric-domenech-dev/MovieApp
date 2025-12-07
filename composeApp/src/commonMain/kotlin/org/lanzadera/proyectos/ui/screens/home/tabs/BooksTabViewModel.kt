package org.lanzadera.proyectos.ui.screens.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.flow.SharingStarted
import org.lanzadera.proyectos.domain.usecase.books.RefreshBooksUseCase
import org.lanzadera.proyectos.ui.mapper.toUI
import org.lanzadera.proyectos.ui.models.BookUI
import org.lanzadera.proyectos.utils.Logger

/**
 * ViewModel for the Books tab.
 * 
 * Manages 8 book categories:
 * - Fiction, Science, History, Biography
 * - Business, Technology, Self-Help, Recent
 */
class BooksTabViewModel(
    private val refreshBooksUseCase: RefreshBooksUseCase?
) : ViewModel() {

    val books: StateFlow<List<BookUI>> = refreshBooksUseCase?.booksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val fictionBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.fictionBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val scienceBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.scienceBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val historyBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.historyBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val biographyBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.biographyBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val businessBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.businessBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val technologyBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.technologyBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val selfHelpBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.selfHelpBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    val recentBooks: StateFlow<List<BookUI>> = refreshBooksUseCase?.recentBooksFlow
        ?.map { it.toUI() }
        ?.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        ?: MutableStateFlow(emptyList())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        refreshIfNeeded()
    }

    /**
     * Refresh books if empty (lazy loading).
     */
    private fun refreshIfNeeded() {
        if (refreshBooksUseCase == null || books.value.isNotEmpty()) {
            Logger.d("Books already loaded or use case not available", tag = "BooksTabViewModel")
            return
        }

        Logger.d("Loading books for all categories", tag = "BooksTabViewModel")
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                supervisorScope {
                    awaitAll(
                        async { refreshBooksUseCase.refreshRecentBooks(force = false) },
                        async { refreshBooksUseCase.refreshFictionBooks(force = false) },
                        async { refreshBooksUseCase.refreshScienceBooks(force = false) },
                        async { refreshBooksUseCase.refreshHistoryBooks(force = false) },
                        async { refreshBooksUseCase.refreshBiographyBooks(force = false) },
                        async { refreshBooksUseCase.refreshBusinessBooks(force = false) },
                        async { refreshBooksUseCase.refreshTechnologyBooks(force = false) },
                        async { refreshBooksUseCase.refreshSelfHelpBooks(force = false) }
                    )
                }
                Logger.d("Books loaded successfully, total: ${books.value.size}", tag = "BooksTabViewModel")
            } catch (t: Throwable) {
                _error.value = t.message ?: "Error fetching books"
                Logger.e("Error refreshing books", tag = "BooksTabViewModel", throwable = t)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    /**
     * Force refresh all book categories.
     */
    fun refresh() {
        if (refreshBooksUseCase == null) return

        Logger.d("Force refreshing books", tag = "BooksTabViewModel")
        viewModelScope.launch {
            try {
                _isRefreshing.value = true
                supervisorScope {
                    awaitAll(
                        async { refreshBooksUseCase.refreshRecentBooks(force = true) },
                        async { refreshBooksUseCase.refreshFictionBooks(force = true) },
                        async { refreshBooksUseCase.refreshScienceBooks(force = true) },
                        async { refreshBooksUseCase.refreshHistoryBooks(force = true) },
                        async { refreshBooksUseCase.refreshBiographyBooks(force = true) },
                        async { refreshBooksUseCase.refreshBusinessBooks(force = true) },
                        async { refreshBooksUseCase.refreshTechnologyBooks(force = true) },
                        async { refreshBooksUseCase.refreshSelfHelpBooks(force = true) }
                    )
                }
            } catch (t: Throwable) {
                _error.value = t.message ?: "Error fetching books"
                Logger.e("Error force refreshing books", tag = "BooksTabViewModel", throwable = t)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
