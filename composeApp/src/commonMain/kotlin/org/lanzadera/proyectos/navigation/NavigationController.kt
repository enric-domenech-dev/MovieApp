package org.lanzadera.proyectos.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.models.movie.Movie
import org.lanzadera.proyectos.utils.Constants

class NavigationController() : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<Constants.NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    lateinit var selectedMovie: Movie

    fun navigateToHome() {
        viewModelScope.launch {
            _navigationEvent.emit(Constants.NavigationEvent.NavigateToHome)
        }
    }

    fun navigateToLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(Constants.NavigationEvent.NavigateToLogin)
        }
    }

    fun navigateToSignIn() {
        viewModelScope.launch {
            _navigationEvent.emit(Constants.NavigationEvent.NavigateToRegister)
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.emit(Constants.NavigationEvent.NavigateBack)
        }
    }

    fun navigateToDetail(movie: Movie) {
        selectedMovie = movie
        viewModelScope.launch {
            _navigationEvent.emit(Constants.NavigationEvent.NavigateToDetail)
        }
    }

}