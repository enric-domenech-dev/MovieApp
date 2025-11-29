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
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.models.book.Book
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.models.tvshow.NextEpisodeInfo
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.models.tvshow.TvShowWithNextEpisode
import org.lanzadera.proyectos.domain.repository.FavoriteDetailsRepository
import org.lanzadera.proyectos.domain.repository.WatchedEpisodesRepository
import org.lanzadera.proyectos.domain.usecase.books.RefreshBooksUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.games.RefreshGamesUseCase
import org.lanzadera.proyectos.domain.usecase.load_initial_data.GetInitialDataUseCase
import org.lanzadera.proyectos.domain.usecase.tvshows.RefreshTvShowsUseCase
import kotlin.coroutines.cancellation.CancellationException

class HomeViewModel(
    private val getInitialData: GetInitialDataUseCase,
    private val refreshBooksUseCase: RefreshBooksUseCase?,
    private val refreshTvShowsUseCase: RefreshTvShowsUseCase? = null,
    private val refreshGamesUseCase: RefreshGamesUseCase? = null,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val watchedEpisodesRepository: WatchedEpisodesRepository,
    private val favoriteDetailsRepository: FavoriteDetailsRepository
) : ViewModel() {

    // HomeTab: ahora con 5 pestañas: FOLLOWING, BOOKS, FILMS, SERIES, GAMES
    enum class HomeTab { FOLLOWING, BOOKS, FILMS, SERIES, GAMES }

    // --- todos los flows, calientes y listos (desde el use case) ---
    val movies = getInitialData.moviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val trendingWeek = getInitialData.trendingMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val trendingDay = getInitialData.trendingMoviesDailyFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val popular = getInitialData.popularMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val topRated = getInitialData.topRatedMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val upcoming = getInitialData.upcomingMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val discover = getInitialData.discoverMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val hero = getInitialData.heroMoviesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val inCinemasToday = getInitialData.inCinemasTodayFlow
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

    val favorites: StateFlow<List<FavoriteItem>> = observeFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Flows para el tab FOLLOWING - usando Room directamente
    val allWatchedEpisodes = watchedEpisodesRepository.observeAllWatchedEpisodes()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val seriesWithUnwatchedEpisodes: StateFlow<List<TvShowWithNextEpisode>> = combine(
        favoriteDetailsRepository.observeFavoriteTvShows(),
        allWatchedEpisodes
    ) { favoriteTvShows, watched ->
        println("SIGUIENDO: Total favorite shows from Room: ${favoriteTvShows.size}, Watched episodes: ${watched.size}")

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        favoriteTvShows.mapNotNull { show ->
            val showId = show.id?.toString() ?: return@mapNotNull null
            val seasons = show.seasons ?: return@mapNotNull null

            println("SIGUIENDO: Analizando ${show.name} (ID: $showId)")

            // Encontrar el próximo episodio sin ver
            val nextEpisode = findNextUnwatchedEpisode(seasons, showId, watched, today)

            if (nextEpisode != null) {
                println("SIGUIENDO:   - Próximo episodio: ${nextEpisode.episodeCode} - ${nextEpisode.displayText}")
                TvShowWithNextEpisode(show, nextEpisode)
            } else {
                println("SIGUIENDO:   - No hay más episodios por ver")
                null
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private fun findNextUnwatchedEpisode(
        seasons: List<org.lanzadera.proyectos.domain.models.tvshow.Season>,
        showId: String,
        watched: List<WatchedEpisode>,
        today: LocalDate
    ): NextEpisodeInfo? {
        // Ordenar temporadas por número (ignorar temporada 0)
        val sortedSeasons = seasons.filter { (it.seasonNumber ?: 0) > 0 }.sortedBy { it.seasonNumber ?: 0 }

        for (season in sortedSeasons) {
            val seasonNumber = season.seasonNumber ?: continue
            val episodes = season.episodes ?: continue

            // Ordenar episodios por número
            val sortedEpisodes = episodes.sortedBy { it.episodeNumber ?: 0 }

            for (episode in sortedEpisodes) {
                val episodeNumber = episode.episodeNumber ?: continue

                // Verificar si está visto
                val isWatched = watched.any {
                    it.tvShowId == showId &&
                            it.seasonNumber == seasonNumber &&
                            it.episodeNumber == episodeNumber
                }

                if (!isWatched) {
                    // Este es el próximo episodio sin ver
                    val airDate = episode.airDate
                    val airDateParsed = airDate?.let { parseDateString(it) }
                    val isAired =
                        airDateParsed?.let { it <= today } ?: true // Si no hay fecha, asumir que está disponible
                    val daysUntilAir = if (airDateParsed != null && !isAired) {
                        (airDateParsed.toEpochDays() - today.toEpochDays()).toInt()
                    } else null

                    return NextEpisodeInfo(
                        seasonNumber = seasonNumber,
                        episodeNumber = episodeNumber,
                        episodeName = episode.name,
                        airDate = airDate,
                        isAired = isAired,
                        daysUntilAir = daysUntilAir
                    )
                }
            }
        }

        return null
    }

    private fun parseDateString(dateString: String): LocalDate? {
        return try {
            if (dateString.isBlank()) return null
            val parts = dateString.split("-")
            if (parts.size == 3) {
                LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
            } else null
        } catch (e: Exception) {
            null
        }
    }

    val upcomingFavoriteMovies: StateFlow<List<org.lanzadera.proyectos.domain.models.movie.Movie>> =
        favoriteDetailsRepository.observeUpcomingFavoriteMovies(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        ).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val moviesWithReleaseInfo: StateFlow<List<org.lanzadera.proyectos.domain.models.movie.MovieWithReleaseInfo>> =
        combine(
            favoriteDetailsRepository.observeFavoriteMovies(),
            movies
        ) { favoriteMovies, allMovies ->
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

            favoriteMovies.mapNotNull { movie ->
                val releaseDate = movie.releaseDate
                val releaseDateParsed = releaseDate?.let { parseDateString(it) }
                val isReleased = releaseDateParsed?.let { it <= today } ?: true
                val daysUntilRelease = if (releaseDateParsed != null && !isReleased) {
                    (releaseDateParsed.toEpochDays() - today.toEpochDays()).toInt()
                } else null

                val releaseInfo = org.lanzadera.proyectos.domain.models.movie.ReleaseInfo(
                    releaseDate = releaseDate,
                    isReleased = isReleased,
                    daysUntilRelease = daysUntilRelease
                )

                org.lanzadera.proyectos.domain.models.movie.MovieWithReleaseInfo(movie, releaseInfo)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val refreshing = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    // Solo una primera carga
    var didFirstLoad = false

    // tab seleccionado (lo guarda el VM; la UI solo lo notifica)
    private val _selectedTab = MutableStateFlow(HomeTab.FOLLOWING)
    val selectedTab: StateFlow<HomeTab> = _selectedTab

    fun selectTab(index: Int) {
        _selectedTab.value = when (index) {
            0 -> HomeTab.FOLLOWING
            1 -> HomeTab.BOOKS
            2 -> HomeTab.FILMS
            3 -> HomeTab.SERIES
            else -> HomeTab.GAMES
        }

        // Si selecciona FOLLOWING y aún no hay datos de series, cargarlas
        if (_selectedTab.value == HomeTab.FOLLOWING) {
            println("SYNCRO HomeViewModel: FOLLOWING tab selected, tvShows.size=${tvShows.value.size}")
            viewModelScope.launch {
                try {
                    if (refreshTvShowsUseCase != null && tvShows.value.isEmpty()) {
                        println("SYNCRO HomeViewModel: launching refreshTvShowsUseCase for FOLLOWING tab")
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
                        println("SYNCRO HomeViewModel: refreshTvShowsUseCase finished for FOLLOWING, tvShows.size=${tvShows.value.size}")
                    } else {
                        println("SYNCRO HomeViewModel: no refresh needed for FOLLOWING or no use case")
                    }
                } catch (t: Throwable) {
                    error.value = t.message ?: "Error fetching tv shows"
                    println("SYNCRO HomeViewModel: error refreshing tv shows for FOLLOWING: ${t.message}")
                } finally {
                    refreshing.value = false
                }
            }
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

    fun toggleFavorite(item: FavoriteItem) {
        viewModelScope.launch { toggleFavoriteUseCase(item) }
    }

    // clearError removed: UI will reset `error` directly (vm.error.value = null) to avoid unused warnings

    // UiState mínimo
    val uiState: StateFlow<UiState> =
        combine(refreshing, error, movies) { isRefreshing, err, mov ->
            val hasContent = mov.isNotEmpty()
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
                    // Cargar series para el tab FOLLOWING inicial
                    selectTab(0)
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

        // Los datos ya están siendo cargados por SplashViewModel en background
        // Aquí solo marcamos que no estamos en estado de carga
        refreshing.value = false
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
        val activeFilters: Set<Int> = emptySet(),
        val onError: Boolean = error != null
    ) {

    }
}
