package com.betterlifeapps.chessclock.ui.settings.edit

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.domain.GameMode
import com.betterlifeapps.chessclock.domain.TimeControl
import com.betterlifeapps.std.BaseViewModel
import com.betterlifeapps.std.ResourceResolver
import com.betterlifeapps.std.ext.getCompletedOrDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class EditViewModel @Inject constructor(
    private val gameModeRepository: GameModeRepository,
    private val resourceResolver: ResourceResolver
) :
    BaseViewModel() {
    val name = MutableStateFlow("")
    val nameErrorText = MutableStateFlow("")
    val player1Mode = MutableStateFlow<TimerMode>(getDefaultTimerMode())
    val player2Mode = MutableStateFlow<TimerMode>(getDefaultTimerMode())

    val commandFlow = MutableSharedFlow<EditScreenCommands>()
    private val gameModeNames: Deferred<List<String>> =
        viewModelScope.async { gameModeRepository.getGameModeNames() }

    private fun getDefaultTimerMode() = TimerMode.TimeAddition("03:00", "00:02")

    fun updateName(newName: String) {
        val validationError = validateName(newName)
        name.value = newName
        nameErrorText.value = validationError?.let {
            resourceResolver.getString(it.errorRes)
        } ?: ""
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
                gameModeRepository.createGameMode(gameMode)
            }.onSuccess {
                commandFlow.emit(EditScreenCommands.Finish)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun onBackClicked() {
        runCoroutine { commandFlow.emit(EditScreenCommands.Finish) }
    }

    private fun validateName(name: String): NameValidationError? {
        return if (name.isBlank()) {
            NameValidationError.CANNOT_BE_EMPTY
        } else if (gameModeNames.getCompletedOrDefault(emptyList()).any { it == name }) {
            NameValidationError.ALREADY_EXISTS
        } else {
            null
        }
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

    enum class NameValidationError(@StringRes val errorRes: Int) {
        CANNOT_BE_EMPTY(R.string.error_empty_name),
        ALREADY_EXISTS(R.string.error_game_mode_already_exists)
    }

    sealed class EditScreenCommands {
        object Finish : EditScreenCommands()
    }
}