package com.betterlifeapps.chessclock.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betterlifeapps.chessclock.domain.GameState
import com.betterlifeapps.chessclock.domain.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private var timerJob: Job? = null

    val gameState = MutableStateFlow(
        getDefaultGameState()
    )

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent

    fun onControlButtonClicked() {
        gameState.value = gameState.value.copy(isPaused = !gameState.value.isPaused)
        if (gameState.value.isPaused) {
            timerJob?.cancel()
        } else {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(100L)
                    val isFirstPlayerTurn = gameState.value.isFirstPlayerTurn
                    val currentPlayerState = if (isFirstPlayerTurn) {
                        gameState.value.player1
                    } else {
                        gameState.value.player2
                    }

                    val newPlayerState =
                        currentPlayerState.copy(timeMillis = currentPlayerState.timeMillis - 100L)
                    if (newPlayerState.timeMillis <= 0) {
                        _uiEvent.emit(UiEvent.TimeExpired(gameState.value.isFirstPlayerTurn))
                        gameState.value = gameState.value.copy(
                            isPaused = true
                        )
                        cancel()
                    }
                    gameState.value = if (isFirstPlayerTurn) {
                        gameState.value.copy(player1 = newPlayerState)
                    } else {
                        gameState.value.copy(player2 = newPlayerState)
                    }
                }
            }
        }
    }

    fun onPlayer1Clicked() {
        if (!gameState.value.isFirstPlayerTurn || gameState.value.isPaused) {
            return
        }

        val isFirstPlayerTurn = gameState.value.isFirstPlayerTurn
        val player1State = gameState.value.player1
        gameState.value = gameState.value.copy(
            player1 = player1State.copy(turn = player1State.turn + 1),
            isFirstPlayerTurn = !isFirstPlayerTurn
        )
    }

    fun onPlayer2Clicked() {
        if (gameState.value.isFirstPlayerTurn || gameState.value.isPaused) {
            return
        }

        val isFirstPlayerTurn = gameState.value.isFirstPlayerTurn
        val player2State = gameState.value.player2
        gameState.value = gameState.value.copy(
            player2 = player2State.copy(turn = player2State.turn + 1),
            isFirstPlayerTurn = !isFirstPlayerTurn
        )
    }

    fun onRestartClicked() {
        gameState.value = getDefaultGameState()
    }

    private fun getDefaultGameState() = GameState(
//        PlayerState(30 * 60 * 1000L, 0),
        PlayerState( 10 * 1000L, 0),
//        PlayerState(30 * 60 * 1000, 0),
        PlayerState(10 * 1000, 0),
        isFirstPlayerTurn = true,
        isPaused = true
    )

    sealed class UiEvent {
        data class TimeExpired(val isFirstPlayerTurn: Boolean) : UiEvent()
    }
}