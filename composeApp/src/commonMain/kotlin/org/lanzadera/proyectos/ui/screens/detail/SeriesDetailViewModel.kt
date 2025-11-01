package org.lanzadera.proyectos.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.usecase.tvshows.RefreshTvShowsUseCase

class SeriesDetailViewModel(
    private val refreshTvShowsUseCase: RefreshTvShowsUseCase
) : ViewModel() {

    private val _tvShowDetail = MutableStateFlow<TvShow?>(null)
    val tvShowDetail: StateFlow<TvShow?> = _tvShowDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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
}

