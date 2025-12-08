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
import org.lanzadera.proyectos.domain.models.WatchedEpisode
import org.lanzadera.proyectos.domain.models.favorite.FavoriteItem
import org.lanzadera.proyectos.domain.models.favorite.FavoriteType
import org.lanzadera.proyectos.domain.models.tvshow.TvShow
import org.lanzadera.proyectos.domain.usecase.episodes.ObserveWatchedEpisodesUseCase
import org.lanzadera.proyectos.domain.usecase.episodes.ToggleEpisodeWatchedUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ObserveFavoritesUseCase
import org.lanzadera.proyectos.domain.usecase.favorites.ToggleTvShowFavoriteUseCase
import org.lanzadera.proyectos.domain.usecase.tvshows.GetTvShowDetailsUseCase
import org.lanzadera.proyectos.ui.mapper.toDetailUI
import org.lanzadera.proyectos.ui.models.TvShowDetailUI

class SeriesDetailViewModel(
    observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleTvShowFavoriteUseCase: ToggleTvShowFavoriteUseCase,
    private val observeWatchedEpisodesUseCase: ObserveWatchedEpisodesUseCase,
    private val toggleEpisodeWatchedUseCase: ToggleEpisodeWatchedUseCase,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase
) : ViewModel() {

    // Internal domain model state
    private val _tvShowDetailDomain = MutableStateFlow<TvShow?>(null)
    
    // Public UI model state - mapped from domain
    val tvShowDetail: StateFlow<TvShowDetailUI?> = _tvShowDetailDomain
        .map { it?.toDetailUI() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val favorites = observeFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _watchedEpisodes = MutableStateFlow<List<WatchedEpisode>>(emptyList())
    val watchedEpisodes: StateFlow<List<WatchedEpisode>> = _watchedEpisodes.asStateFlow()

    fun loadTvShowDetails(tvShowId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = getTvShowDetailsUseCase(tvShowId)) {
                is org.lanzadera.proyectos.domain.models.Result.Success -> {
                    _tvShowDetailDomain.value = result.data
                    if (result.data == null) {
                        _error.value = "No se pudieron cargar los detalles de la serie"
                    } else {
                        // Observar episodios vistos en un Job separado
                        viewModelScope.launch {
                            observeWatchedEpisodesUseCase(tvShowId.toString())
                                .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
                                .collect { _watchedEpisodes.value = it }
                        }
                    }
                }
                is org.lanzadera.proyectos.domain.models.Result.Error -> {
                    _error.value = result.message ?: "Error al cargar los detalles"
                }
                is org.lanzadera.proyectos.domain.models.Result.Loading -> {
                    // Not used in this use case
                }
            }
            
            _isLoading.value = false
        }
    }

    fun setTvShowDetail(tvShow: TvShow) {
        _tvShowDetailDomain.value = tvShow
        tvShow.id?.let {
            loadTvShowDetails(it)
        }
    }

    fun toggleFavorite() {
        val tvShow = _tvShowDetailDomain.value ?: return
        val item = FavoriteItem(
            id = tvShow.id?.toString() ?: return,
            type = FavoriteType.TV_SHOW,
            title = tvShow.name ?: tvShow.originalName.orEmpty(),
            posterUrl = tvShow.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
            overview = tvShow.overview
        )
        viewModelScope.launch { toggleTvShowFavoriteUseCase(item) }
    }

    fun toggleEpisodeWatched(seasonNumber: Int, episodeNumber: Int, isWatched: Boolean) {
        val tvShowId = _tvShowDetailDomain.value?.id?.toString() ?: return
        val tvShow = _tvShowDetailDomain.value ?: return

        viewModelScope.launch {
            if (isWatched) {
                // Marcar este episodio y todos los anteriores como vistos
                val episodesToMark = mutableListOf<WatchedEpisode>()

                tvShow.seasons?.forEach { season ->
                    val currentSeasonNumber = season.seasonNumber ?: return@forEach

                    // Para temporadas anteriores a la actual, marcar todos los episodios
                    if (currentSeasonNumber < seasonNumber) {
                        season.episodes?.forEach { episode ->
                            val epNum = episode.episodeNumber ?: return@forEach
                            episodesToMark.add(
                                WatchedEpisode(
                                    tvShowId = tvShowId,
                                    seasonNumber = currentSeasonNumber,
                                    episodeNumber = epNum
                                )
                            )
                        }
                    }
                    // Para la temporada actual, marcar solo los episodios hasta el seleccionado
                    else if (currentSeasonNumber == seasonNumber) {
                        season.episodes?.forEach { episode ->
                            val epNum = episode.episodeNumber ?: return@forEach
                            if (epNum <= episodeNumber) {
                                episodesToMark.add(
                                    WatchedEpisode(
                                        tvShowId = tvShowId,
                                        seasonNumber = currentSeasonNumber,
                                        episodeNumber = epNum
                                    )
                                )
                            }
                        }
                    }
                }

                // Marcar todos los episodios recopilados
                episodesToMark.forEach { episode ->
                    toggleEpisodeWatchedUseCase(episode, true) // Result ignored - errors logged in use case
                }
            } else {
                // Al desmarcar, desmarcar este episodio y todos los posteriores
                val episodesToUnmark = mutableListOf<WatchedEpisode>()

                tvShow.seasons?.forEach { season ->
                    val currentSeasonNumber = season.seasonNumber ?: return@forEach

                    // Para temporadas posteriores a la actual, desmarcar todos los episodios
                    if (currentSeasonNumber > seasonNumber) {
                        season.episodes?.forEach { episode ->
                            val epNum = episode.episodeNumber ?: return@forEach
                            episodesToUnmark.add(
                                WatchedEpisode(
                                    tvShowId = tvShowId,
                                    seasonNumber = currentSeasonNumber,
                                    episodeNumber = epNum
                                )
                            )
                        }
                    }
                    // Para la temporada actual, desmarcar solo los episodios desde el seleccionado en adelante
                    else if (currentSeasonNumber == seasonNumber) {
                        season.episodes?.forEach { episode ->
                            val epNum = episode.episodeNumber ?: return@forEach
                            if (epNum >= episodeNumber) {
                                episodesToUnmark.add(
                                    WatchedEpisode(
                                        tvShowId = tvShowId,
                                        seasonNumber = currentSeasonNumber,
                                        episodeNumber = epNum
                                    )
                                )
                            }
                        }
                    }
                }

                // Desmarcar todos los episodios recopilados
                episodesToUnmark.forEach { episode ->
                    toggleEpisodeWatchedUseCase(episode, false) // Result ignored - errors logged in use case
                }
            }
        }
    }

    fun isEpisodeWatched(seasonNumber: Int, episodeNumber: Int): Boolean {
        return _watchedEpisodes.value.any {
            it.seasonNumber == seasonNumber && it.episodeNumber == episodeNumber
        }
    }
}
