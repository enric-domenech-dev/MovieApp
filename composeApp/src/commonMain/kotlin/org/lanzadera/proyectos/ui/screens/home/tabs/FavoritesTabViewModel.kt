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
import org.lanzadera.proyectos.ui.mapper.toUI
import org.lanzadera.proyectos.ui.models.FavoriteItemUI
import org.lanzadera.proyectos.ui.models.FavoriteItemWithInfoUI
import org.lanzadera.proyectos.ui.models.FavoriteTypeUI
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
    private val observeWatchedMoviesUseCase: ObserveWatchedMoviesUseCase
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
            val nextEpisode = findNextUnwatchedEpisode(seasons, showId, watched, today)

            if (nextEpisode != null) {
                Logger.d("SIGUIENDO:   - Próximo episodio: ${nextEpisode.episodeCode} - ${nextEpisode.displayText}", tag = "FavoritesTabViewModel")
                TvShowWithNextEpisode(show, nextEpisode)
            } else {
                Logger.d("SIGUIENDO:   - No hay más episodios por ver", tag = "FavoritesTabViewModel")
                null
            }
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

            // If no next episode, it's finished
            findNextUnwatchedEpisode(seasons, showId, watched, today) == null
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
        finishedSeries
    ) { movies, series, watchedMovies, finishedShows ->
        val movieItems = movies.map { movieWithRelease ->
            FavoriteItemWithInfo.MovieItem(
                movieWithRelease = movieWithRelease,
                id = movieWithRelease.movie.id?.toString() ?: "",
                posterUrl = movieWithRelease.movie.posterPath,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
        }

        val seriesItems = series.map { tvShowWithNext ->
            FavoriteItemWithInfo.TvShowItem(
                tvShowWithNext = tvShowWithNext,
                id = tvShowWithNext.tvShow.id?.toString() ?: "",
                posterUrl = tvShowWithNext.tvShow.posterPath,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
        }

        val watchedMovieItems = watchedMovies.map { movie ->
            FavoriteItemWithInfo.WatchedMovieItem(
                movie = movie,
                id = movie.id?.toString() ?: "",
                posterUrl = movie.posterPath,
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

        // Sort: unwatched available first, then by days until available, then completed
        val sortedDomainItems = (movieItems + seriesItems + watchedMovieItems + finishedSeriesItems).sortedWith(
            compareBy<FavoriteItemWithInfo> { it.isCompleted }
                .thenByDescending { item -> if (!item.isCompleted) item.isAvailable else false }
                .thenBy { item ->
                    if (!item.isCompleted && !item.isAvailable) {
                        item.daysUntilAvailable ?: Int.MAX_VALUE
                    } else {
                        -1
                    }
                }
        )
        
        // Map to UI models
        sortedDomainItems.map { it.toUI() }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Filter states
    private val _showMovies = MutableStateFlow(false)
    val showMovies: StateFlow<Boolean> = _showMovies.asStateFlow()
    
    private val _showSeries = MutableStateFlow(true)
    val showSeries: StateFlow<Boolean> = _showSeries.asStateFlow()

    // Filtered favorites based on active filters
    val filteredFavoritesWithInfo: StateFlow<List<FavoriteItemWithInfoUI>> = combine(
        favoritesWithInfo,
        showMovies,
        showSeries
    ) { items, moviesEnabled, seriesEnabled ->
        when {
            !moviesEnabled && !seriesEnabled -> emptyList() // No filters active
            moviesEnabled && seriesEnabled -> items // All items
            moviesEnabled -> items.filter { 
                it is FavoriteItemWithInfoUI.MovieItem || 
                it is FavoriteItemWithInfoUI.WatchedMovieItem 
            }
            seriesEnabled -> items.filter { 
                it is FavoriteItemWithInfoUI.TvShowItem || 
                it is FavoriteItemWithInfoUI.FinishedSeriesItem 
            }
            else -> emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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
        _showMovies.value = !_showMovies.value
    }

    /**
     * Toggle series filter.
     */
    fun toggleSeriesFilter() {
        _showSeries.value = !_showSeries.value
    }

    /**
     * Find the next unwatched episode for a TV show.
     * 
     * @return NextEpisodeInfo if found, null if all episodes are watched
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
            val episodes = season.episodes ?: continue

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
                    // This is the next unwatched episode
                    val airDate = episode.airDate
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
