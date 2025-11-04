package org.lanzadera.proyectos.ui.screens.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.lanzadera.proyectos.domain.models.game.Game
import org.lanzadera.proyectos.domain.usecase.games.GetGameDetailsUseCase

class GameDetailViewModel(
    private val getGameDetailsUseCase: GetGameDetailsUseCase
) : ViewModel() {

    private val _gameDetails = MutableStateFlow<Game?>(null)
    val gameDetails: StateFlow<Game?> = _gameDetails

    fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            try {
                val game = getGameDetailsUseCase(gameId)
                _gameDetails.value = game
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

