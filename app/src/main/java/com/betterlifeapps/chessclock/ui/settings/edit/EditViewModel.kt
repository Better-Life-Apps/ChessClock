package com.betterlifeapps.chessclock.ui.settings.edit

import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.domain.GameMode
import com.betterlifeapps.chessclock.domain.TimeControl
import com.betterlifeapps.std.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class EditViewModel @Inject constructor(val gameModeRepository: GameModeRepository) :
    BaseViewModel() {
    val name = MutableStateFlow("")
    val player1Mode = MutableStateFlow<TimerMode>(getDefaultTimerMode())
    val player2Mode = MutableStateFlow<TimerMode>(getDefaultTimerMode())

    val commandFlow = MutableSharedFlow<EditScreenCommands>()

    private fun getDefaultTimerMode() = TimerMode.TimeAddition("03:00", "00:02")

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updatePlayer1Mode(newMode: TimerMode) {
        player1Mode.value = newMode
    }

    fun updatePlayer2Mode(newMode: TimerMode) {
        player2Mode.value = newMode
    }

    fun onDoneButtonClicked() {
        runCoroutine {
            runCatching {
                val player1TimeControl = player1Mode.value.toTimeControl()
                val player2TimeControl = player2Mode.value.toTimeControl()
                val gameMode = GameMode(
                    0,
                    false,
                    name.value,
                    false,
                    OffsetDateTime.now(ZoneId.of("UTC")),
                    player1TimeControl,
                    player2TimeControl
                )
                gameModeRepository.saveGameMode(gameMode)
            }
                .onSuccess {
                    commandFlow.emit(EditScreenCommands.Finish)
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    fun onBackClicked() {
        runCoroutine { commandFlow.emit(EditScreenCommands.Finish) }
    }

    private fun TimerMode.toTimeControl() = when (this) {
        is TimerMode.ConstantTime -> TimeControl.ConstantTimeControl(timeToLong(timePerTurn))
        is TimerMode.TimeAddition -> TimeControl.AdditionTimeControl(
            timeToLong(startTime),
            timeToLong(addition)
        )
        is TimerMode.NoAddition -> TimeControl.NoAdditionTimeControl(timeToLong(startTime))
    }

    private fun timeToLong(time: String): Long {
        return time.substringBefore(':').toLong() * 60 * 1000 + time.substringAfter(':')
            .toLong() * 1000
    }

    sealed class EditScreenCommands {
        object Finish : EditScreenCommands()
    }
}