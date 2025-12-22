package org.lanzadera.proyectos.ui.screens.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItemWithInfo
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.models.movie.MovieWithReleaseInfo
import org.lanzadera.proyectos.domain.models.movie.ReleaseInfo
import org.lanzadera.proyectos.domain.models.tvshow.NextEpisodeInfo
import org.lanzadera.proyectos.domain.models.tvshow.Season
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.models.tvshow.TvShowWithNextEpisode
import org.lanzadera.proyectos.domain.usecase.episodes.ObserveAllWatchedEpisodesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.GetFavoriteDetailsUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleBookFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleGameFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleMovieFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleTvShowFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.movies.ObserveWatchedMoviesUseCase
import org.lanzadera.proyectos.domain.usecase.settings.ObserveContentFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.ObserveMoviesFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.ObserveSeriesFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.UpdateContentFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.UpdateMoviesFiltersUseCase
import org.lanzadera.proyectos.domain.usecase.settings.UpdateSeriesFiltersUseCase
import org.lanzadera.proyectos.ui.mapper.toUI
import org.lanzadera.proyectos.ui.models.FavoriteItemUI
import org.lanzadera.proyectos.ui.models.FavoriteItemWithInfoUI
import org.lanzadera.proyectos.ui.models.FavoriteTypeUI
import org.lanzadera.proyectos.ui.models.shouldShowWithFilters
import org.lanzadera.proyectos.ui.utils.normalizeImageUrl
import org.lanzadera.proyectos.utils.DateUtils
import org.lanzadera.proyectos.utils.Logger
import kotlin.coroutines.cancellation.CancellationException
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem as DomainFavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType as DomainFavoriteType

/**
 * ViewModel for the Favorites (Following) tab.
 * 
 * Manages:
 * - Favorite items tracking
 * - TV shows with next unwatched episodes
 * - Movies with release date tracking
 * - Watched content
 * - Toggle favorite actions
 */
class FavoritesTabViewModel(
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleMovieFavoriteUseCase: ToggleMovieFavoriteUseCase,
    private val toggleTvShowFavoriteUseCase: ToggleTvShowFavoriteUseCase,
    private val toggleBookFavoriteUseCase: ToggleBookFavoriteUseCase,
    private val toggleGameFavoriteUseCase: ToggleGameFavoriteUseCase,
    private val observeAllWatchedEpisodesUseCase: ObserveAllWatchedEpisodesUseCase,
    private val getFavoriteDetailsUseCase: GetFavoriteDetailsUseCase,
    private val observeWatchedMoviesUseCase: ObserveWatchedMoviesUseCase,
    private val observeSeriesFiltersUseCase: ObserveSeriesFiltersUseCase,
    private val observeMoviesFiltersUseCase: ObserveMoviesFiltersUseCase,
    private val updateSeriesFiltersUseCase: UpdateSeriesFiltersUseCase,
    private val updateMoviesFiltersUseCase: UpdateMoviesFiltersUseCase,
    private val observeContentFiltersUseCase: ObserveContentFiltersUseCase,
    private val updateContentFiltersUseCase: UpdateContentFiltersUseCase
) : ViewModel() {

    // Basic favorites list
    val favorites: StateFlow<List<FavoriteItemUI>> = observeFavoritesUseCase()
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // All watched episodes (for tracking progress)
    val allWatchedEpisodes: StateFlow<List<WatchedEpisode>> = 
        observeAllWatchedEpisodesUseCase()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // TV Shows with next unwatched episode
    data class EpisodeCheckResult(val nextEpisode: NextEpisodeInfo?, val isInProduction: Boolean)
    
    val seriesWithUnwatchedEpisodes: StateFlow<List<TvShowWithNextEpisode>> = combine(
        getFavoriteDetailsUseCase.observeFavoriteTvShows(),
        allWatchedEpisodes
    ) { favoriteTvShows, watched ->
        Logger.d("SIGUIENDO: Total favorite shows from Room: ${favoriteTvShows.size}, Watched episodes: ${watched.size}", tag = "FavoritesTabViewModel")

        val today = DateUtils.getTodayInUserTimezone()

        favoriteTvShows.mapNotNull { show ->
            val showId = show.id?.toString() ?: return@mapNotNull null
            val seasons = show.seasons ?: return@mapNotNull null

            Logger.d("SIGUIENDO: Analizando ${show.name} (ID: $showId)", tag = "FavoritesTabViewModel")

            // Find next unwatched episode
            val result = findNextUnwatchedEpisodeWithStatus(seasons, showId, watched, today)

            if (result.nextEpisode != null) {
                Logger.d(
                    "SIGUIENDO:   - Próximo episodio: ${result.nextEpisode.episodeCode} - ${result.nextEpisode.displayText}",
                    tag = "FavoritesTabViewModel"
                )
                TvShowWithNextEpisode(show, result.nextEpisode)
            } else {
                Logger.d(
                    "SIGUIENDO:   - No hay más episodios por ver (en producción: ${result.isInProduction})",
                    tag = "FavoritesTabViewModel"
                )
                null
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Series in production (no episodes with air dates yet)
    val seriesInProduction: StateFlow<List<TvShow>> = combine(
        getFavoriteDetailsUseCase.observeFavoriteTvShows(),
        allWatchedEpisodes
    ) { favoriteTvShows, watched ->
        val today = DateUtils.getTodayInUserTimezone()

        favoriteTvShows.filter { show ->
            val showId = show.id?.toString() ?: return@filter false
            val seasons = show.seasons ?: return@filter false

            val result = findNextUnwatchedEpisodeWithStatus(seasons, showId, watched, today)
            result.nextEpisode == null && result.isInProduction
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Finished series (all episodes watched)
    val finishedSeries: StateFlow<List<TvShow>> = combine(
        getFavoriteDetailsUseCase.observeFavoriteTvShows(),
        allWatchedEpisodes
    ) { favoriteTvShows, watched ->
        val today = DateUtils.getTodayInUserTimezone()

        favoriteTvShows.filter { show ->
            val showId = show.id?.toString() ?: return@filter false
            val seasons = show.seasons ?: return@filter false

            val result = findNextUnwatchedEpisodeWithStatus(seasons, showId, watched, today)
            result.nextEpisode == null && !result.isInProduction
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Upcoming favorite movies
    val upcomingFavoriteMovies: StateFlow<List<Movie>> =
        getFavoriteDetailsUseCase.observeUpcomingFavoriteMovies(
            DateUtils.getTodayInUserTimezone().toString()
        ).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Movies with release date tracking
    val moviesWithReleaseInfo: StateFlow<List<MovieWithReleaseInfo>> =
        combine(
            getFavoriteDetailsUseCase.observeFavoriteMovies(),
            observeWatchedMoviesUseCase()
        ) { favoriteMovies, watchedMovies ->
            val today = DateUtils.getTodayInUserTimezone()
            val watchedMovieIds = watchedMovies.map { it.movieId }.toSet()

            favoriteMovies
                .filter { movie ->
                    val movieId = movie.id?.toString() ?: return@filter false
                    !watchedMovieIds.contains(movieId)
                }
                .mapNotNull { movie ->
                    val releaseDate = movie.releaseDate
                    val isReleased = DateUtils.hasDatePassed(releaseDate)
                    val daysUntilRelease = DateUtils.daysUntilDate(releaseDate, adjustForTimezone = true)?.let { days ->
                        if (days > 0) days else null
                    }

                    val releaseInfo = ReleaseInfo(
                        releaseDate = releaseDate,
                        isReleased = isReleased,
                        daysUntilRelease = daysUntilRelease
                    )

                    MovieWithReleaseInfo(movie, releaseInfo)
                }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Watched movies
    val watchedMoviesWithInfo: StateFlow<List<Movie>> =
        combine(
            getFavoriteDetailsUseCase.observeFavoriteMovies(),
            observeWatchedMoviesUseCase()
        ) { favoriteMovies, watchedMovies ->
            val watchedMovieIds = watchedMovies.map { it.movieId }.toSet()

            favoriteMovies.filter { movie ->
                val movieId = movie.id?.toString() ?: return@filter false
                watchedMovieIds.contains(movieId)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Combined favorites with all info (sorted by availability)
    val favoritesWithInfo: StateFlow<List<FavoriteItemWithInfoUI>> = combine(
        moviesWithReleaseInfo,
        seriesWithUnwatchedEpisodes,
        watchedMoviesWithInfo,
        finishedSeries,
        seriesInProduction
    ) { movies, series, watchedMovies, finishedShows, inProductionShows ->
        val movieItems = movies.map { movieWithRelease ->
            FavoriteItemWithInfo.MovieItem(
                movieWithRelease = movieWithRelease,
                id = movieWithRelease.movie.id?.toString() ?: "",
                posterUrl = movieWithRelease.movie.posterPath?.let { normalizeImageUrl(it) }, 
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
        }

        val seriesItems = series.map { tvShowWithNext ->
            FavoriteItemWithInfo.TvShowItem(
                tvShowWithNext = tvShowWithNext,
                id = tvShowWithNext.tvShow.id?.toString() ?: "",
                posterUrl = tvShowWithNext.tvShow.posterPath?.let { normalizeImageUrl(it) }, 
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
        }

        val watchedMovieItems = watchedMovies.map { movie ->
            FavoriteItemWithInfo.WatchedMovieItem(
                movie = movie,
                id = movie.id?.toString() ?: "",
                posterUrl = movie.posterPath?.let { normalizeImageUrl(it) }, 
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
        }

        val finishedSeriesItems = finishedShows.map { tvShow ->
            FavoriteItemWithInfo.FinishedSeriesItem(
                tvShow = tvShow,
                id = tvShow.id?.toString() ?: "",
                posterUrl = tvShow.posterPath,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
        }

        val inProductionSeriesItems = inProductionShows.map { tvShow ->
            FavoriteItemWithInfo.InProductionSeriesItem(
                tvShow = tvShow,
                id = tvShow.id?.toString() ?: "",
                posterUrl = tvShow.posterPath,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
        }

        // Sort: 1) Disponibles, 2) Próximamente (por días), 3) En Producción, 4) Series Finalizadas sin episodios pendientes
        val sortedDomainItems =
            (movieItems + seriesItems + watchedMovieItems + finishedSeriesItems + inProductionSeriesItems).sortedWith(
                compareBy<FavoriteItemWithInfo> { item ->
                    // Check if series is ended/cancelled AND has no more episodes to watch
                    val isSeriesEndedAndCompleted = when (item) {
                        is FavoriteItemWithInfo.TvShowItem -> {
                            // Si tiene próximo episodio, no está completada aunque esté ended
                            false
                        }

                        is FavoriteItemWithInfo.InProductionSeriesItem -> {
                            // Series en producción nunca están completadas
                            false
                        }

                        is FavoriteItemWithInfo.FinishedSeriesItem -> {
                            // Si está en FinishedSeriesItem, no tiene más episodios por ver
                            val status = item.tvShow.status?.lowercase()
                            status == "ended" || status == "canceled" || status == "cancelled"
                        }

                        is FavoriteItemWithInfo.WatchedMovieItem -> false
                        is FavoriteItemWithInfo.MovieItem -> false
                    }

                    when {
                        isSeriesEndedAndCompleted -> 4 // Series finalizadas SIN episodios por ver
                        item.isAvailable && !item.isCompleted -> 0 // Disponibles primero (incluye ended con episodios)
                        item.daysUntilAvailable != null -> 1 // Próximamente segundo
                        else -> 2 // En producción tercero
                    }
                }.thenBy { item ->
                    // Dentro de "Próximamente", ordenar por días ascendente
                    if (!item.isCompleted && !item.isAvailable && item.daysUntilAvailable != null) {
                        item.daysUntilAvailable
                    } else {
                        Int.MAX_VALUE
                    }
                }
        )
        
        // Map to UI models
        sortedDomainItems.map { it.toUI() }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Filter states for content types (persisted in Settings)
    // showMovies and showSeries are persisted via use cases so the chips selection survives restarts
    val showMovies: StateFlow<Boolean> = observeContentFiltersUseCase
        .observeShowMovies()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val showSeries: StateFlow<Boolean> = observeContentFiltersUseCase
        .observeShowSeries()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Filter states for series status (desde repositorio)
    val showAvailableSeries: StateFlow<Boolean> = observeSeriesFiltersUseCase
        .observeShowAvailableSeries()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val showUpcomingSeries: StateFlow<Boolean> = observeSeriesFiltersUseCase
        .observeShowUpcomingSeries()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val showInProductionSeries: StateFlow<Boolean> = observeSeriesFiltersUseCase
        .observeShowInProductionSeries()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val showEndedSeries: StateFlow<Boolean> = observeSeriesFiltersUseCase
        .observeShowEndedSeries()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Filter states for movies status (desde repositorio)
    val showAvailableMovies: StateFlow<Boolean> = observeMoviesFiltersUseCase
        .observeShowAvailableMovies()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val showUpcomingMovies: StateFlow<Boolean> = observeMoviesFiltersUseCase
        .observeShowUpcomingMovies()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Filtered favorites based on active filters
    val filteredFavoritesWithInfo: StateFlow<List<FavoriteItemWithInfoUI>> = favoritesWithInfo
        .combine(showMovies) { items, movies -> items to movies }
        .combine(showSeries) { (items, movies), series -> Triple(items, movies, series) }
        .combine(showAvailableSeries) { (items, movies, series), availSeries ->
            items to mapOf(
                "movies" to movies,
                "series" to series,
                "availSeries" to availSeries
            )
        }
        .combine(showUpcomingSeries) { (items, map), upSeries ->
            items to (map + ("upSeries" to upSeries))
        }
        .combine(showInProductionSeries) { (items, map), prodSeries ->
            items to (map + ("prodSeries" to prodSeries))
        }
        .combine(showEndedSeries) { (items, map), endSeries ->
            items to (map + ("endSeries" to endSeries))
        }
        .combine(showAvailableMovies) { (items, map), availMovies ->
            items to (map + ("availMovies" to availMovies))
        }
        .combine(showUpcomingMovies) { (items, map), upMovies ->
            val moviesEnabled = map["movies"] as Boolean
            val seriesEnabled = map["series"] as Boolean
            val availableSeries = map["availSeries"] as Boolean
            val upcomingSeries = map["upSeries"] as Boolean
            val inProductionSeries = map["prodSeries"] as Boolean
            val endedSeries = map["endSeries"] as Boolean
            val availableMovies = map["availMovies"] as Boolean
            val upcomingMovies = upMovies

            when {
                !moviesEnabled && !seriesEnabled -> emptyList() // No filters active
                moviesEnabled && seriesEnabled -> items.filter { item ->
                    item.shouldShowWithFilters(
                        availableSeries, upcomingSeries, inProductionSeries, endedSeries,
                        availableMovies, upcomingMovies
                    )
                }

                moviesEnabled -> items.filter { item ->
                    (item is FavoriteItemWithInfoUI.MovieItem || item is FavoriteItemWithInfoUI.WatchedMovieItem) &&
                            item.shouldShowWithFilters(
                                availableSeries, upcomingSeries, inProductionSeries, endedSeries,
                                availableMovies, upcomingMovies
                            )
                }

                seriesEnabled -> items.filter { item ->
                    (item is FavoriteItemWithInfoUI.TvShowItem ||
                            item is FavoriteItemWithInfoUI.FinishedSeriesItem ||
                            item is FavoriteItemWithInfoUI.InProductionSeriesItem) &&
                            item.shouldShowWithFilters(
                                availableSeries, upcomingSeries, inProductionSeries, endedSeries,
                                availableMovies, upcomingMovies
                            )
                }

                else -> emptyList()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Loading and error states
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Toggle favorite status for an item.
     */
    fun toggleFavorite(item: FavoriteItemUI) {
        viewModelScope.launch {
            try {
                val domainItem = DomainFavoriteItem(
                    id = item.id,
                    type = when(item.type) {
                        FavoriteTypeUI.MOVIE -> DomainFavoriteType.MOVIE
                        FavoriteTypeUI.TV_SHOW -> DomainFavoriteType.TV_SHOW
                        FavoriteTypeUI.GAME -> DomainFavoriteType.GAME
                        FavoriteTypeUI.BOOK -> DomainFavoriteType.BOOK
                    },
                    title = item.title,
                    posterUrl = item.posterUrl,
                    addedAt = Instant.fromEpochMilliseconds(item.addedAt)
                )
                
                // Use specific use case based on type
                when(item.type) {
                    FavoriteTypeUI.MOVIE -> toggleMovieFavoriteUseCase(domainItem)
                    FavoriteTypeUI.TV_SHOW -> toggleTvShowFavoriteUseCase(domainItem)
                    FavoriteTypeUI.BOOK -> toggleBookFavoriteUseCase(domainItem)
                    FavoriteTypeUI.GAME -> toggleGameFavoriteUseCase(domainItem)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Logger.e(tag = "FavoritesTabViewModel", message = "Error toggling favorite", throwable = e)
                _error.value = "Failed to toggle favorite: ${e.message}"
            }
        }
    }

    /**
     * Clear error message.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Toggle movies filter.
     */
    fun toggleMoviesFilter() {
        viewModelScope.launch {
            updateMoviesFiltersUseCase.updateShowAvailableMovies(!showMovies.value)
            // Persist the chip selection
            updateContentFiltersUseCase.updateShowMovies(!showMovies.value)
        }
    }

    /**
     * Toggle series filter.
     */
    fun toggleSeriesFilter() {
        viewModelScope.launch {
            // Persist the chip selection
            updateContentFiltersUseCase.updateShowSeries(!showSeries.value)
        }
    }

    /**
     * Toggle series status filters.
     */
    fun toggleAvailableSeries() {
        viewModelScope.launch {
            updateSeriesFiltersUseCase.updateShowAvailableSeries(!showAvailableSeries.value)
        }
    }

    fun toggleUpcomingSeries() {
        viewModelScope.launch {
            updateSeriesFiltersUseCase.updateShowUpcomingSeries(!showUpcomingSeries.value)
        }
    }

    fun toggleInProductionSeries() {
        viewModelScope.launch {
            updateSeriesFiltersUseCase.updateShowInProductionSeries(!showInProductionSeries.value)
        }
    }

    fun toggleEndedSeries() {
        viewModelScope.launch {
            updateSeriesFiltersUseCase.updateShowEndedSeries(!showEndedSeries.value)
        }
    }

    /**
     * Toggle movies status filters.
     */
    fun toggleAvailableMovies() {
        viewModelScope.launch {
            updateMoviesFiltersUseCase.updateShowAvailableMovies(!showAvailableMovies.value)
        }
    }

    fun toggleUpcomingMovies() {
        viewModelScope.launch {
            updateMoviesFiltersUseCase.updateShowUpcomingMovies(!showUpcomingMovies.value)
        }
    }

    /**
     * Find the next unwatched episode for a TV show with production status.
     *
     * @return EpisodeCheckResult with next episode info and production status
     */
    private fun findNextUnwatchedEpisodeWithStatus(
        seasons: List<Season>,
        showId: String,
        watched: List<WatchedEpisode>,
        today: LocalDate
    ): EpisodeCheckResult {
        // Sort seasons by number (ignore season 0 - specials)
        val sortedSeasons = seasons.filter { (it.seasonNumber ?: 0) > 0 }.sortedBy { it.seasonNumber ?: 0 }

        for (season in sortedSeasons) {
            val seasonNumber = season.seasonNumber ?: continue
            val episodes = season.episodes

            // Skip seasons with 0 episodes (in production, not yet available)
            if (episodes.isNullOrEmpty()) {
                val episodeCount = season.episodeCount ?: 0
                if (episodeCount == 0) {
                    Logger.d(
                        "SIGUIENDO:   - Temporada $seasonNumber tiene 0 episodios (en producción)",
                        tag = "FavoritesTabViewModel"
                    )
                    return EpisodeCheckResult(nextEpisode = null, isInProduction = true)
                }
                continue
            }

            // Sort episodes by number
            val sortedEpisodes = episodes.sortedBy { it.episodeNumber ?: 0 }

            for (episode in sortedEpisodes) {
                val episodeNumber = episode.episodeNumber ?: continue

                // Check if watched
                val isWatched = watched.any {
                    it.tvShowId == showId &&
                            it.seasonNumber == seasonNumber &&
                            it.episodeNumber == episodeNumber
                }

                if (!isWatched) {
                    // Check if episode has an air date
                    val airDate = episode.airDate
                    if (airDate == null) {
                        Logger.d(
                            "SIGUIENDO:   - Episodio S${seasonNumber}E${episodeNumber} sin fecha de emisión (en producción)",
                            tag = "FavoritesTabViewModel"
                        )
                        return EpisodeCheckResult(nextEpisode = null, isInProduction = true)
                    }

                    // This is the next unwatched episode with a valid air date
                    val isAired = DateUtils.hasDatePassed(airDate)
                    val daysUntilAir = DateUtils.daysUntilDate(airDate, adjustForTimezone = true)?.let { days ->
                        if (days > 0) days else null
                    }

                    return EpisodeCheckResult(
                        nextEpisode = NextEpisodeInfo(
                            seasonNumber = seasonNumber,
                            episodeNumber = episodeNumber,
                            episodeName = episode.name,
                            airDate = airDate,
                            isAired = isAired,
                            daysUntilAir = daysUntilAir
                        ),
                        isInProduction = false
                    )
                }
            }
        }

        // All episodes watched, not in production
        return EpisodeCheckResult(nextEpisode = null, isInProduction = false)
    }

    /**
     * Find the next unwatched episode for a TV show.
     *
     * @return NextEpisodeInfo if found, null if all episodes are watched or series is in production without release dates
     */
    private fun findNextUnwatchedEpisode(
        seasons: List<Season>,
        showId: String,
        watched: List<WatchedEpisode>,
        today: LocalDate
    ): NextEpisodeInfo? {
        // Sort seasons by number (ignore season 0 - specials)
        val sortedSeasons = seasons.filter { (it.seasonNumber ?: 0) > 0 }.sortedBy { it.seasonNumber ?: 0 }

        for (season in sortedSeasons) {
            val seasonNumber = season.seasonNumber ?: continue
            val episodes = season.episodes

            // Skip seasons with 0 episodes (in production, not yet available)
            if (episodes.isNullOrEmpty()) {
                val episodeCount = season.episodeCount ?: 0
                if (episodeCount == 0) {
                    Logger.d(
                        "SIGUIENDO:   - Temporada $seasonNumber tiene 0 episodios (en producción)",
                        tag = "FavoritesTabViewModel"
                    )
                    return null
                }
                continue
            }

            // Sort episodes by number
            val sortedEpisodes = episodes.sortedBy { it.episodeNumber ?: 0 }

            for (episode in sortedEpisodes) {
                val episodeNumber = episode.episodeNumber ?: continue

                // Check if watched
                val isWatched = watched.any {
                    it.tvShowId == showId &&
                            it.seasonNumber == seasonNumber &&
                            it.episodeNumber == episodeNumber
                }

                if (!isWatched) {
                    // Check if episode has an air date
                    val airDate = episode.airDate
                    if (airDate == null) {
                        Logger.d(
                            "SIGUIENDO:   - Episodio S${seasonNumber}E${episodeNumber} sin fecha de emisión (en producción)",
                            tag = "FavoritesTabViewModel"
                        )
                        return null
                    }

                    // This is the next unwatched episode with a valid air date
                    val isAired = DateUtils.hasDatePassed(airDate)
                    val daysUntilAir = DateUtils.daysUntilDate(airDate, adjustForTimezone = true)?.let { days ->
                        if (days > 0) days else null
                    }

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
}
