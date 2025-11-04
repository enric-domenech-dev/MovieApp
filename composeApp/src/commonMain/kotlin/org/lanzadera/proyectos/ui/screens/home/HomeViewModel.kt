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
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.usecase.books.RefreshBooksUseCase
import org.lanzadera.proyectos.domain.usecase.games.RefreshGamesUseCase
import org.lanzadera.proyectos.domain.usecase.load_initial_data.LoadInitialDataUseCase
import org.lanzadera.proyectos.domain.usecase.tvshows.RefreshTvShowsUseCase
import kotlin.coroutines.cancellation.CancellationException

class HomeViewModel(
    private val loadInitialData: LoadInitialDataUseCase,
    private val refreshBooksUseCase: RefreshBooksUseCase?,
    private val refreshTvShowsUseCase: RefreshTvShowsUseCase? = null,
    private val refreshGamesUseCase: RefreshGamesUseCase? = null
) : ViewModel() {

    // HomeTab: ahora con 5 pestañas: BOOKS, FILMS, SERIES, GAMES, <3
    enum class HomeTab { BOOKS, FILMS, SERIES, GAMES, HEART }

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

    // Books flow (if use case provided)
    val books: StateFlow<List<Book>> = refreshBooksUseCase?.booksFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    // TvShows flows (if use case provided)
    val tvShows: StateFlow<List<TvShow>> = refreshTvShowsUseCase?.tvShowsFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())
    val popularTvShows: StateFlow<List<TvShow>> = refreshTvShowsUseCase?.popularTvShowsFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())
    val topRatedTvShows: StateFlow<List<TvShow>> = refreshTvShowsUseCase?.topRatedTvShowsFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())
    val onAirTvShows: StateFlow<List<TvShow>> = refreshTvShowsUseCase?.onAirTvShowsFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())
    val trendingTvShows: StateFlow<List<TvShow>> = refreshTvShowsUseCase?.trendingTvShowsFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    val airingTodayTvShows: StateFlow<List<TvShow>> = refreshTvShowsUseCase?.airingTodayTvShowsFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    val trendingTvShowsWeek: StateFlow<List<TvShow>> = refreshTvShowsUseCase?.trendingTvShowsWeekFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    // Derived TV show flows for additional sections
    val airingTodayAndTrendingTvShows: StateFlow<List<TvShow>> =
        combine(onAirTvShows, airingTodayTvShows) { onAir, airingToday ->
            (onAir + airingToday).distinctBy { it.id }.take(20)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val recommendedTvShows: StateFlow<List<TvShow>> = combine(topRatedTvShows, popularTvShows) { topRated, popular ->
        (topRated + popular).distinctBy { it.id }.shuffled().take(20)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val upcomingTvShows: StateFlow<List<TvShow>> = tvShows.combine(onAirTvShows) { all, onAir ->
        all.filter { it !in onAir }.take(20)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Games flows - obtained from RefreshGamesUseCase which exposes repository flows
    val games: StateFlow<List<Game>> = refreshGamesUseCase?.gamesFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    val popularGames: StateFlow<List<Game>> = refreshGamesUseCase?.popularGamesFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    val topRatedGames: StateFlow<List<Game>> = refreshGamesUseCase?.topRatedGamesFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    val upcomingGames: StateFlow<List<Game>> = refreshGamesUseCase?.upcomingGamesFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    val trendingGames: StateFlow<List<Game>> = refreshGamesUseCase?.trendingGamesFlow
        ?.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        ?: MutableStateFlow(emptyList())

    val refreshing = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    // Solo una primera carga
    var didFirstLoad = false

    // tab seleccionado (lo guarda el VM; la UI solo lo notifica)
    private val _selectedTab = MutableStateFlow(HomeTab.FILMS)
    val selectedTab: StateFlow<HomeTab> = _selectedTab

    fun selectTab(index: Int) {
        _selectedTab.value = when (index) {
            0 -> HomeTab.BOOKS
            1 -> HomeTab.FILMS
            2 -> HomeTab.SERIES
            3 -> HomeTab.GAMES
            else -> HomeTab.HEART
        }

        // Si selecciona BOOKS y aún no hay datos, lanzar refresco
        if (_selectedTab.value == HomeTab.BOOKS) {
            println("SYNCRO HomeViewModel: BOOKS tab selected, books.size=${books.value.size}")
            viewModelScope.launch {
                try {
                    if (refreshBooksUseCase != null && books.value.isEmpty()) {
                        println("SYNCRO HomeViewModel: launching refreshBooksUseCase.refreshBooks()")
                        refreshing.value = true
                        refreshBooksUseCase.refreshBooks(force = false, query = "")
                        println("SYNCRO HomeViewModel: refreshBooksUseCase finished, books.size=${books.value.size}")
                    } else {
                        println("SYNCRO HomeViewModel: no refresh needed or no use case")
                    }
                } catch (t: Throwable) {
                    error.value = t.message ?: "Error fetching books"
                    println("SYNCRO HomeViewModel: error refreshing books: ${t.message}")
                } finally {
                    refreshing.value = false
                }
            }
        }

        // Si selecciona SERIES y aún no hay datos, lanzar refresco
        if (_selectedTab.value == HomeTab.SERIES) {
            println("SYNCRO HomeViewModel: SERIES tab selected, tvShows.size=${tvShows.value.size}")
            viewModelScope.launch {
                try {
                    if (refreshTvShowsUseCase != null && tvShows.value.isEmpty()) {
                        println("SYNCRO HomeViewModel: launching refreshTvShowsUseCase for all TV endpoints")
                        refreshing.value = true
                        supervisorScope {
                            awaitAll(
                                async { refreshTvShowsUseCase.refreshTvShows(force = false) },
                                async { refreshTvShowsUseCase.refreshPopularTvShows(force = false) },
                                async { refreshTvShowsUseCase.refreshTopRatedTvShows(force = false) },
                                async { refreshTvShowsUseCase.refreshOnAirTvShows(force = false) },
                                async { refreshTvShowsUseCase.refreshTrendingTvShows(force = false) },
                                async { refreshTvShowsUseCase.refreshAiringTodayTvShows(force = false) },
                                async { refreshTvShowsUseCase.refreshTrendingTvShowsWeek(force = false) }
                            )
                        }
                        println("SYNCRO HomeViewModel: refreshTvShowsUseCase finished, tvShows.size=${tvShows.value.size}")
                    } else {
                        println("SYNCRO HomeViewModel: no refresh needed or no use case")
                    }
                } catch (t: Throwable) {
                    error.value = t.message ?: "Error fetching tv shows"
                    println("SYNCRO HomeViewModel: error refreshing tv shows: ${t.message}")
                } finally {
                    refreshing.value = false
                }
            }
        }

        // Si selecciona GAMES y aún no hay datos, lanzar refresco
        if (_selectedTab.value == HomeTab.GAMES) {
            println("SYNCRO HomeViewModel: GAMES tab selected, games.size=${games.value.size}")
            viewModelScope.launch {
                try {
                    if (refreshGamesUseCase != null && games.value.isEmpty()) {
                        println("SYNCRO HomeViewModel: launching refreshGamesUseCase")
                        refreshing.value = true
                        refreshGamesUseCase.invoke()
                        println("SYNCRO HomeViewModel: refreshGamesUseCase finished, games.size=${games.value.size}")
                    } else {
                        println("SYNCRO HomeViewModel: no refresh needed or no game use case")
                    }
                } catch (t: Throwable) {
                    if (t !is CancellationException) {
                        error.value = t.message ?: "Error fetching games"
                        println("SYNCRO HomeViewModel: error refreshing games: ${t.message}")
                    }
                } finally {
                    refreshing.value = false
                }
            }
        }
    }

    // clearError removed: UI will reset `error` directly (vm.error.value = null) to avoid unused warnings

    // mapping de tab -> par de listas (primary y secondary)
    private fun feedsFor(tab: HomeTab): Pair<Flow<List<Movie>>, Flow<List<Movie>>> =
        when (tab) {
            HomeTab.BOOKS -> trendingDay to inCinemasToday     // placeholder: Books will be shown in a different UI; keep compatibility
            HomeTab.FILMS -> movies to movies                 // FILMS mostrará secciones separadas en la UI
            HomeTab.SERIES -> trendingWeek to upcoming        // placeholder si aún no hay series
            HomeTab.GAMES -> hero to discover                 // placeholder
            HomeTab.HEART -> discover to discover            // placeholder
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
