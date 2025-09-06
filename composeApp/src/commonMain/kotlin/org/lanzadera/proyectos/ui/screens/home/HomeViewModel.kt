package org.lanzadera.proyectos.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.usecase.load_initial_data.LoadInitialDataUseCase

class HomeViewModel(
    private val loadInitialData: LoadInitialDataUseCase
) : ViewModel() {

    // Flows de dominio -> StateFlow
    val movies: StateFlow<List<Movie>> =
        loadInitialData.moviesFlow
            .onStart {
                // Carga inicial automática al arrancar la VM
                refreshIfNeeded()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val trending: StateFlow<List<Movie>> =
        loadInitialData.trendingMoviesFlow
            .onStart {
                // Carga inicial automática al arrancar la VM
                refreshIfNeeded()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Flags propios de la VM
    private val loading = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    // Evita refrescos concurrentes
    private val refreshMutex = Mutex()

    // UiState mínimo, sin listas
    val uiState: StateFlow<UiState> =
        combine(loading, error) { isLoading, err ->
            UiState(isLoading = isLoading, error = err)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState(isLoading = true)
        )

    private fun refreshIfNeeded(force: Boolean = false) {
        viewModelScope.launch {
            // nota: si ya estás refrescando, sal temprano
            if (loading.value) return@launch

            refreshMutex.withLock {
                val alreadyLoaded = movies.value.isNotEmpty() && trending.value.isNotEmpty()
                if (!force && alreadyLoaded) return@withLock

                loading.value = true
                error.value = null
                try {
                    // Lanza ambas recargas en paralelo
                    awaitAll(
                        async { loadInitialData.refreshMovies() },
                        async { loadInitialData.refreshTrendingMovies() }
                    )
                    // si OK, limpia error explícitamente
                    error.value = null
                } catch (t: Throwable) {
                    error.value = t.message ?: "Ha ocurrido un error"
                } finally {
                    loading.value = false
                }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null
    )
}