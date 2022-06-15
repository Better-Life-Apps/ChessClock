package com.betterlifeapps.chessclock.ui.game

import androidx.lifecycle.viewModelScope
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.domain.GameMode
import com.betterlifeapps.chessclock.domain.GameState
import com.betterlifeapps.chessclock.domain.PlayerState
import com.betterlifeapps.chessclock.domain.State
import com.betterlifeapps.chessclock.domain.TimeControl
import com.betterlifeapps.std.BaseViewModel
import com.betterlifeapps.std.common.UiEvent
import com.betterlifeapps.std.common.UiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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

    val gameState = MutableStateFlow(getDefaultGameState())

    fun onControlButtonClicked() {
        if (gameState.value.state == State.FINISHED) {
            restart()
        } else {
            toggleGameState()
        }
    }

    private fun toggleGameState() {
        val newState = if (gameState.value.state == State.RUNNING) State.PAUSED else State.RUNNING
        gameState.value =
            gameState.value.copy(state = newState)
        if (gameState.value.state == State.RUNNING) {
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
                        currentPlayerState.copy(
                            timeMillis = (currentPlayerState.timeMillis - 100L).coerceAtLeast(
                                0L
                            )
                        )
                    if (newPlayerState.timeMillis <= 0) {
                        onTimeExpired()
                    }
                    gameState.value = if (isFirstPlayerTurn) {
                        gameState.value.copy(player1 = newPlayerState)
                    } else {
                        gameState.value.copy(player2 = newPlayerState)
                    }
                }
            }
        } else {
            timerJob?.cancel()
        }
    }

    private fun onTimeExpired() {
        postUiEvent(GameScreenUiEvent.TimeExpired(gameState.value.isFirstPlayerTurn))
        postUiEvent(PlaySoundRes(R.raw.time_over))
        gameState.value = gameState.value.copy(state = State.FINISHED)
        timerJob?.cancel()
    }

    fun onPlayer1Clicked() {
        if (!gameState.value.isFirstPlayerTurn || gameState.value.state != State.RUNNING) {
            return
        }

        val isFirstPlayerTurn = gameState.value.isFirstPlayerTurn
        val player1State = gameState.value.player1
        val playerTimeControl = gameMode.value?.player1TimeControl
        val newTimeMillis = if (playerTimeControl is TimeControl.ConstantTimeControl) {
            playerTimeControl.timePerTurn
        } else {
            val additionMillis = playerTimeControl?.additionTime ?: 0
            player1State.timeMillis + additionMillis
        }
        gameState.value = gameState.value.copy(
            player1 = player1State.copy(
                turn = player1State.turn + 1,
                timeMillis = newTimeMillis
            ),
            isFirstPlayerTurn = !isFirstPlayerTurn
        )

        postUiEvent(PlaySoundRes(R.raw.click_sound))
    }

    fun onPlayer2Clicked() {
        if (gameState.value.isFirstPlayerTurn || gameState.value.state != State.RUNNING) {
            return
        }

        val isFirstPlayerTurn = gameState.value.isFirstPlayerTurn
        val player2State = gameState.value.player2
        val playerTimeControl = gameMode.value?.player2TimeControl
        val newTimeMillis = if (playerTimeControl is TimeControl.ConstantTimeControl) {
            playerTimeControl.timePerTurn
        } else {
            val additionMillis = playerTimeControl?.additionTime ?: 0
            player2State.timeMillis + additionMillis
        }
        gameState.value = gameState.value.copy(
            player2 = player2State.copy(
                turn = player2State.turn + 1,
                timeMillis = newTimeMillis
            ),
            isFirstPlayerTurn = !isFirstPlayerTurn
        )

        postUiEvent(PlaySoundRes(R.raw.click_sound))
    }

    private fun getGameStateFromGameMode(gameMode: GameMode): GameState {
        val player1StartTime = gameMode.player1TimeControl.startTime
        val player2StartTime = gameMode.player2TimeControl.startTime
        return GameState(
            PlayerState(player1StartTime, 0),
            PlayerState(player2StartTime, 0),
            isFirstPlayerTurn = true,
            state = State.READY
        )
    }

    fun onRestartClicked() {
        when (gameState.value.state) {
            State.PAUSED -> {
                postUiEvent(GameScreenUiEvent.ShowConfirmationDialog(
                    onConfirmClicked = {
                        restart()
                        postUiEvent(GameScreenUiEvent.Restart)
                    }
                ))
            }
            State.FINISHED -> {
                restart()
                postUiEvent(GameScreenUiEvent.Restart)
            }
            else -> Unit
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
        state = State.READY
    )
    sealed class GameScreenUiEvent : UiEvent() {
        data class TimeExpired(val isFirstPlayerTurn: Boolean) : GameScreenUiEvent()
        object Restart : GameScreenUiEvent()
        data class ShowConfirmationDialog(val onConfirmClicked: () -> Unit) : GameScreenUiEvent()
    }
}