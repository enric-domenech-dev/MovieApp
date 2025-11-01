package org.lanzadera.proyectos.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.repository.SearchRepository

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<List<Any>>(emptyList())
    val results: StateFlow<List<Any>> = _results.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun performSearch() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isEmpty()) {
            _results.value = emptyList()
            _error.value = null
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Buscar películas y series en paralelo
                val movies = searchRepository.searchMovies(currentQuery)
                val tvShows = searchRepository.searchTvShows(currentQuery)

                // Combinar resultados alternando películas y series
                val combinedResults = mutableListOf<Any>()
                val maxSize = maxOf(movies.size, tvShows.size)

                for (i in 0 until maxSize) {
                    if (i < movies.size) {
                        combinedResults.add(movies[i])
                    }
                    if (i < tvShows.size) {
                        combinedResults.add(tvShows[i])
                    }
                }

                _results.value = combinedResults

                if (combinedResults.isEmpty()) {
                    _error.value = "No se encontraron resultados para '$currentQuery'"
                }
            } catch (e: Exception) {
                _error.value = "Error al buscar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clear() {
        _query.value = ""
        _results.value = emptyList()
        _error.value = null
    }
}

