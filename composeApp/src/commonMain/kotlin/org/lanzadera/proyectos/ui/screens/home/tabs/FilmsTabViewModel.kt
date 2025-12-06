package org.lanzadera.proyectos.ui.screens.home.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import org.lanzadera.proyectos.domain.usecase.load_initial_data.GetInitialDataUseCase
import org.lanzadera.proyectos.ui.mapper.toUI
import org.lanzadera.proyectos.ui.models.MovieUI

/**
 * ViewModel for the Films tab.
 * 
 * Displays 9 movie sections:
 * - Discover, Trending (week/day), Popular, Top Rated
 * - Upcoming, Hero, In Cinemas Today
 * 
 * Note: Data is pre-loaded by SplashViewModel via GetInitialDataUseCase.
 */
class FilmsTabViewModel(
    private val getInitialData: GetInitialDataUseCase
) : ViewModel() {

    val movies: StateFlow<List<MovieUI>> = getInitialData.moviesFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val trendingWeek: StateFlow<List<MovieUI>> = getInitialData.trendingMoviesFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val trendingDay: StateFlow<List<MovieUI>> = getInitialData.trendingMoviesDailyFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val popular: StateFlow<List<MovieUI>> = getInitialData.popularMoviesFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val topRated: StateFlow<List<MovieUI>> = getInitialData.topRatedMoviesFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val upcoming: StateFlow<List<MovieUI>> = getInitialData.upcomingMoviesFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val discover: StateFlow<List<MovieUI>> = getInitialData.discoverMoviesFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val hero: StateFlow<List<MovieUI>> = getInitialData.heroMoviesFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val inCinemasToday: StateFlow<List<MovieUI>> = getInitialData.inCinemasTodayFlow
        .map { it.toUI() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
