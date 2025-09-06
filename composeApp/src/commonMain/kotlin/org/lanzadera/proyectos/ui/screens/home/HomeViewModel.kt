package org.lanzadera.proyectos.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.usecase.load_initial_data.LoadInitialDataUseCase

class HomeViewModel(
    private val loadInitialData: LoadInitialDataUseCase
) : ViewModel() {

    // Los datos se coleccionan siempre; evitamos resuscripciones que reboten el estado.
    val movies: StateFlow<List<Movie>> =
        loadInitialData.moviesFlow
            .stateIn(
                viewModelScope, // Mantener vivo mientras haya collectors
                SharingStarted.Eagerly, // Siempre activo mientras scope vivo
                emptyList()) // Valor inicial

    val trending: StateFlow<List<Movie>> =
        loadInitialData.trendingMoviesFlow
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Flags de UI
    private val refreshing =
        MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)

    // Solo una primera carga
    private var didFirstLoad = false

    // UiState mínimo
    val uiState: StateFlow<UiState> =
        combine(refreshing, error) { isRefreshing, err ->
            UiState(isLoading = isRefreshing, error = err)
        }

            // Primera carga automática al arrancar
            .onStart {
                if (!didFirstLoad) {
                    didFirstLoad = true
                    refreshIfNeeded()
                }
            }

            // Estado inicial: cargando
            .stateIn(viewModelScope, SharingStarted.Eagerly, UiState(isLoading = true))

    private fun refreshIfNeeded(force: Boolean = false) {
        if (refreshing.value) return

        val yaHayDatos = movies.value.isNotEmpty() && trending.value.isNotEmpty()
        if (!force && yaHayDatos) return

        viewModelScope.launch {
            refreshing.value = true
            error.value = null
            try {
                // Importante: estas funciones deben actualizar los StateFlow del repo ANTES de retornar
                awaitAll(
                    async { loadInitialData.refreshMovies() },
                    async { loadInitialData.refreshTrendingMovies() }
                )
                // En este punto el repo ya ha emitido; ahora sí podemos apagar el loading sin huecos.
            } catch (t: Throwable) {
                error.value = t.message ?: "Ha ocurrido un error"
            } finally {
                refreshing.value = false
            }
        }
    }


    data class UiState(
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val firstLoadFinished: Boolean = false,
        val error: String? = null,
        val selectedTab: Int = 0,
        val activeFilters: Set<Int> = emptySet()
    )
}