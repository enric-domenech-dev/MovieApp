package org.lanzadera.proyectos.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.Result
import org.lanzadera.proyectos.domain.usecase.search.SearchMoviesUseCase
import org.lanzadera.proyectos.domain.usecase.search.SearchTvShowsUseCase
import org.lanzadera.proyectos.ui.mapper.toUI

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchTvShowsUseCase: SearchTvShowsUseCase
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
            
            // Search movies and TV shows using use cases
            val moviesResult = searchMoviesUseCase(currentQuery)
            val tvShowsResult = searchTvShowsUseCase(currentQuery)
            
            // Check if either search failed
            if (moviesResult is Result.Error && tvShowsResult is Result.Error) {
                _error.value = "Error al buscar: ${moviesResult.message}"
                _isLoading.value = false
                return@launch
            }
            
            // Extract data from results and map to UI models
            val moviesUI = when (moviesResult) {
                is Result.Success -> moviesResult.data.map { it.toUI() }
                is Result.Error -> emptyList()
                is Result.Loading -> emptyList()
            }
            
            val tvShowsUI = when (tvShowsResult) {
                is Result.Success -> tvShowsResult.data.map { it.toUI() }
                is Result.Error -> emptyList()
                is Result.Loading -> emptyList()
            }

            // Combine results alternating movies and TV shows
            val combinedResults = mutableListOf<Any>()
            val maxSize = maxOf(moviesUI.size, tvShowsUI.size)

            for (i in 0 until maxSize) {
                if (i < moviesUI.size) {
                    combinedResults.add(moviesUI[i])
                }
                if (i < tvShowsUI.size) {
                    combinedResults.add(tvShowsUI[i])
                }
            }

            _results.value = combinedResults

            if (combinedResults.isEmpty()) {
                _error.value = "No se encontraron resultados para '$currentQuery'"
            }
            
            _isLoading.value = false
        }
    }

    fun clear() {
        _query.value = ""
        _results.value = emptyList()
        _error.value = null
    }
}

