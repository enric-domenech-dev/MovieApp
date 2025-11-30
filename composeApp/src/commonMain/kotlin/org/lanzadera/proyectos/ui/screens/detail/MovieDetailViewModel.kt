package org.lanzadera.proyectos.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.MovieRepository
import org.lanzadera.proyectos.domain.repository.WatchedMoviesRepository
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.movies.ToggleMovieWatchedUseCase
import org.lanzadera.proyectos.utils.DateUtils

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val watchedMoviesRepository: WatchedMoviesRepository,
    private val toggleMovieWatchedUseCase: ToggleMovieWatchedUseCase
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<Movie?>(null)
    val movieDetail: StateFlow<Movie?> = _movieDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val favorites = observeFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val watchedMovies = watchedMoviesRepository.observeAllWatchedMovies()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val isWatched: StateFlow<Boolean> = watchedMovies.map { watched ->
        _movieDetail.value?.id?.toString()?.let { movieId ->
            watched.any { it.movieId == movieId }
        } ?: false
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isReleased: StateFlow<Boolean> = _movieDetail.map { movie ->
        DateUtils.hasDatePassed(movie?.releaseDate)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val details = movieRepository.getMovieDetails(movieId)
                _movieDetail.value = details
                if (details == null) {
                    _error.value = "No se pudieron cargar los detalles de la película"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setMovieDetail(movie: Movie) {
        _movieDetail.value = movie
        // Automáticamente cargar detalles completos (cast, crew, etc.)
        movie.id?.let { loadMovieDetails(it) }
    }

    fun toggleFavorite() {
        val movie = _movieDetail.value ?: return
        val item = FavoriteItem(
            id = movie.id?.toString() ?: return,
            type = FavoriteType.MOVIE,
            title = movie.title ?: movie.originalTitle.orEmpty(),
            posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
            overview = movie.overview
        )
        viewModelScope.launch { toggleFavoriteUseCase(item) }
    }

    fun toggleWatched() {
        val movieId = _movieDetail.value?.id?.toString() ?: return
        val currentWatched = isWatched.value
        val movieIsReleased = isReleased.value

        // Solo permitir marcar como visto si ya se estrenó
        if (!movieIsReleased && !currentWatched) {
            return
        }

        viewModelScope.launch {
            toggleMovieWatchedUseCase(movieId, !currentWatched)
        }
    }
}
