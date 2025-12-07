package org.lanzadera.proyectos.ui.screens.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.usecase.games.GetGameDetailsUseCase

class GameDetailViewModel(
    private val getGameDetailsUseCase: GetGameDetailsUseCase
) : ViewModel() {

    private val _gameDetails = MutableStateFlow<Game?>(null)
    val gameDetails: StateFlow<Game?> = _gameDetails.asStateFlow()

    fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            when (val result = getGameDetailsUseCase(gameId)) {
                is org.lanzadera.proyectos.domain.models.Result.Success -> {
                    _gameDetails.value = result.data
                }
                is org.lanzadera.proyectos.domain.models.Result.Error -> {
                    // Error already logged in use case
                }
                is org.lanzadera.proyectos.domain.models.Result.Loading -> {
                    // Not used in this use case
                }
            }
        }
    }
}

