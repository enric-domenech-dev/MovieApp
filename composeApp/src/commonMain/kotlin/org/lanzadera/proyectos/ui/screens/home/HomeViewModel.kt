package org.lanzadera.proyectos.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.usecase.load_initial_data.LoadInitialDataUseCase
import kotlin.coroutines.cancellation.CancellationException

class HomeViewModel(
    private val loadInitialData: LoadInitialDataUseCase
) : ViewModel() {

    // HomeTab: define qué dos feeds se muestran en cada pestaña
    enum class HomeTab { TENDENCIAS, PELICULAS, SERIES, FAVORITOS }

    // --- todos los flows, calientes y listos ---
    val movies = loadInitialData.moviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val trendingWeek = loadInitialData.trendingMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val trendingDay = loadInitialData.trendingMoviesDailyFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val popular = loadInitialData.popularMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val topRated = loadInitialData.topRatedMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val upcoming = loadInitialData.upcomingMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val discover = loadInitialData.discoverMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val hero = loadInitialData.heroMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val inCinemasToday = loadInitialData.inCinemasTodayFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Flags de UI
    val refreshing = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    // Solo una primera carga
    var didFirstLoad = false

    // tab seleccionado (lo guarda el VM; la UI solo lo notifica)
    private val _selectedTab = MutableStateFlow(HomeTab.TENDENCIAS)
    val selectedTab: StateFlow<HomeTab> = _selectedTab

    fun selectTab(index: Int) {
        _selectedTab.value = when (index) {
            0 -> HomeTab.TENDENCIAS
            1 -> HomeTab.PELICULAS
            2 -> HomeTab.SERIES
            else -> HomeTab.FAVORITOS
        }
    }

    fun clearError() {
        error.value = null
    }

    // mapping de tab -> par de listas (primary y secondary)
    private fun feedsFor(tab: HomeTab): Pair<Flow<List<Movie>>, Flow<List<Movie>>> =
        when (tab) {
            HomeTab.TENDENCIAS -> trendingDay to inCinemasToday     // cabecera: hoy / cartelera
            HomeTab.PELICULAS -> popular to topRated               // cabecera: populares / grid: top
            HomeTab.SERIES -> trendingWeek to upcoming          // placeholder si aún no hay series
            HomeTab.FAVORITOS -> hero to discover                  // placeholder hasta tener favoritos
        }

    // listas visibles según el tab (conmutadas sin recarga)
    @OptIn(ExperimentalCoroutinesApi::class)
    val primary: StateFlow<List<Movie>> =
        selectedTab.flatMapLatest { feedsFor(it).first }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val secondary: StateFlow<List<Movie>> =
        selectedTab.flatMapLatest { feedsFor(it).second }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // UiState mínimo
    val uiState: StateFlow<UiState> =
        combine(refreshing, error, primary, secondary) { isRefreshing, err, p, s ->
            val hasContent = p.isNotEmpty() || s.isNotEmpty()
            UiState(
                isLoading = isRefreshing && !hasContent,
                isRefreshing = isRefreshing,
                firstLoadFinished = didFirstLoad && !isRefreshing,
                error = if (!hasContent) err else null
            )
        }
            .onStart {
                if (!didFirstLoad) {
                    didFirstLoad = true
                    refreshIfNeeded()
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                UiState(isLoading = true, isRefreshing = true)
            )


    private fun refreshIfNeeded(force: Boolean = false) {
        if (refreshing.value) return

        val yaHayDatos = movies.value.isNotEmpty()
        if (!force && yaHayDatos) return

        viewModelScope.launch {
            refreshing.value = true
            error.value = null
            try {
                supervisorScope {
                    awaitAll(
                        async { loadInitialData.refreshMovies() },
                        async { loadInitialData.refreshTrendingMovies() },
                        async { loadInitialData.refreshPopularMovies() },
                        async { loadInitialData.refreshTopRatedMovies() },
                        async { loadInitialData.refreshUpcomingMovies() },
                        async { loadInitialData.refreshDiscoverMovies() },
                        async { loadInitialData.refreshHeroMovies() },
                        async { loadInitialData.refreshTrendingMoviesDaily() },
                        async { loadInitialData.refreshInCinemasToday() }
                    )
                }
            } catch (ce: CancellationException) {
                error.value = ce.message ?: "Error desconocido"
            } catch (t: Throwable) {
                error.value = t.message ?: "Error desconocido"
            } finally {
                refreshing.value = false
            }
        }
    }

    fun refresh(force: Boolean) {
        refreshIfNeeded(force)
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


