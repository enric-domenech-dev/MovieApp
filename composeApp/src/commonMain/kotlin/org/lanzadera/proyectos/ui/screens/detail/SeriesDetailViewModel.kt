package org.lanzadera.proyectos.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.tvshows.RefreshTvShowsUseCase

class SeriesDetailViewModel(
    private val refreshTvShowsUseCase: RefreshTvShowsUseCase,
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _tvShowDetail = MutableStateFlow<TvShow?>(null)
    val tvShowDetail: StateFlow<TvShow?> = _tvShowDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val favorites = observeFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun loadTvShowDetails(tvShowId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val details = refreshTvShowsUseCase.getTvShowDetails(tvShowId)
                _tvShowDetail.value = details
                if (details == null) {
                    _error.value = "No se pudieron cargar los detalles de la serie"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setTvShowDetail(tvShow: TvShow) {
        _tvShowDetail.value = tvShow
        // Automáticamente cargar detalles completos (temporadas, episodios, etc.)
        tvShow.id?.let { loadTvShowDetails(it) }
    }

    fun toggleFavorite() {
        val tvShow = _tvShowDetail.value ?: return
        val item = FavoriteItem(
            id = tvShow.id?.toString() ?: return,
            type = FavoriteType.TV_SHOW,
            title = tvShow.name ?: tvShow.originalName.orEmpty(),
            posterUrl = tvShow.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
            overview = tvShow.overview
        )
        viewModelScope.launch { toggleFavoriteUseCase(item) }
    }
}
