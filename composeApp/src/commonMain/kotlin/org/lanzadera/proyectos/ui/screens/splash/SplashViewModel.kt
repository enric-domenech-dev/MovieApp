package org.lanzadera.proyectos.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.lanzadera.proyectos.domain.repository.LoadInitialData

class SplashViewModel(
    private val loadInitialDataRepository: LoadInitialData
) : ViewModel() {

    private val _isLoadingComplete = MutableStateFlow(false)
    val isLoadingComplete: StateFlow<Boolean> = _isLoadingComplete

    init {
        // Inicia la carga de todos los datos en paralelo
        viewModelScope.launch {
            try {
                supervisorScope {
                    awaitAll(
                        async { loadInitialDataRepository.refreshMovies() },
                        async { loadInitialDataRepository.refreshDiscoverMovies() },
                        async { loadInitialDataRepository.refreshPopularMovies() },
                        async { loadInitialDataRepository.refreshTopRatedMovies() },
                        async { loadInitialDataRepository.refreshUpcomingMovies() },
                        async { loadInitialDataRepository.refreshTrendingMovies() },
                        async { loadInitialDataRepository.refreshHeroMovies() },
                        async { loadInitialDataRepository.refreshTrendingMoviesDaily() },
                        async { loadInitialDataRepository.refreshInCinemasToday() }
                    )
                }
                _isLoadingComplete.value = true
            } catch (e: Exception) {
                // Si hay error, marcamos como completo igual para no bloquear la UI
                println("SYNCRO SplashViewModel: Error loading initial data: ${e.message}")
                _isLoadingComplete.value = true
            }
        }
    }
}

