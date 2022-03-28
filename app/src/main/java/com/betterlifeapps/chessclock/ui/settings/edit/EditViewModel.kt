package com.betterlifeapps.chessclock.ui.settings.edit

import com.betterlifeapps.std.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class EditViewModel @Inject constructor() : BaseViewModel() {
    val player1Mode = MutableStateFlow<TimerMode>(getDefaultTimerMode())
    val player2Mode = MutableStateFlow<TimerMode>(getDefaultTimerMode())

    private fun getDefaultTimerMode() = TimerMode.ConstantTime()

    fun updatePlayer1Mode(newMode: TimerMode) {
        player1Mode.value = newMode
    }

    fun updatePlayer2Mode(newMode: TimerMode) {
        player2Mode.value = newMode
    }

    fun onDoneButtonClicked() {

    }
}