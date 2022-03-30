package com.betterlifeapps.chessclock.ui.game

import androidx.lifecycle.viewModelScope
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.domain.GameMode
import com.betterlifeapps.chessclock.domain.GameState
import com.betterlifeapps.chessclock.domain.PlayerState
import com.betterlifeapps.std.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(gameModeRepository: GameModeRepository) :
    BaseViewModel() {

    private val gameMode = gameModeRepository.getSelectedGameMode()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        gameMode.collectFlow {
            it?.let {
                gameState.value = getGameStateFromGameMode(it)
            }
        }
    }

    private var timerJob: Job? = null

    val gameState = MutableStateFlow(
        getDefaultGameState()
    )

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent

    fun onControlButtonClicked() {
        gameState.value =
            gameState.value.copy(isPaused = !gameState.value.isPaused, isStarted = true)
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
        val additionMillis = gameMode.value?.player1TimeControl?.additionTime ?: 0
        gameState.value = gameState.value.copy(
            player1 = player1State.copy(
                turn = player1State.turn + 1,
                timeMillis = player1State.timeMillis + additionMillis
            ),
            isFirstPlayerTurn = !isFirstPlayerTurn
        )
    }

    fun onPlayer2Clicked() {
        if (gameState.value.isFirstPlayerTurn || gameState.value.isPaused) {
            return
        }

        val isFirstPlayerTurn = gameState.value.isFirstPlayerTurn
        val player2State = gameState.value.player2
        val additionMillis = gameMode.value?.player2TimeControl?.additionTime ?: 0
        gameState.value = gameState.value.copy(
            player2 = player2State.copy(
                turn = player2State.turn + 1,
                timeMillis = player2State.timeMillis + additionMillis
            ),
            isFirstPlayerTurn = !isFirstPlayerTurn
        )
    }

    private fun getGameStateFromGameMode(gameMode: GameMode): GameState {
        val player1StartTime = gameMode.player1TimeControl.startTime
        val player2StartTime = gameMode.player2TimeControl.startTime
        return GameState(
            PlayerState(player1StartTime, 0),
            PlayerState(player2StartTime, 0),
            isFirstPlayerTurn = true,
            isPaused = true,
            isStarted = false
        )
    }

    fun onRestartClicked() {
        if (gameState.value.isStarted) {
            runCoroutine {
                _uiEvent.emit(UiEvent.ShowConfirmationDialog(
                    onConfirmClicked = {
                        restart()
                        runCoroutine { _uiEvent.emit(UiEvent.Restart) }
                    }
                ))
            }
        }
    }

    private fun restart() {
        gameMode.value?.let {
            gameState.value = getGameStateFromGameMode(it)
        }
    }

    private fun getDefaultGameState() = GameState(
        PlayerState(10 * 1000L, 0),
        PlayerState(10 * 1000, 0),
        isFirstPlayerTurn = true,
        isPaused = true,
        isStarted = false
    )

    sealed class UiEvent {
        data class TimeExpired(val isFirstPlayerTurn: Boolean) : UiEvent()
        object Restart : UiEvent()
        data class ShowConfirmationDialog(val onConfirmClicked: () -> Unit) : UiEvent()
    }
}