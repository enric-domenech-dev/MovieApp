package org.lanzadera.proyectos.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.movie.Movie
import org.lanzadera.proyectos.domain.repository.MovieRepository

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<Movie?>(null)
    val movieDetail: StateFlow<Movie?> = _movieDetail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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
}

