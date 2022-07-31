package com.betterlifeapps.chessclock.ui.menu.settings

import com.betterlifeapps.chessclock.common.Constants.DEFAULT_SOUND_GAME_OVER
import com.betterlifeapps.chessclock.common.Constants.DEFAULT_SOUND_TURN
import com.betterlifeapps.chessclock.common.Constants.DEFAULT_VIBRATION_GAME_OVER
import com.betterlifeapps.chessclock.common.Constants.KEY_SOUND_GAME_OVER
import com.betterlifeapps.chessclock.common.Constants.KEY_SOUND_TURN
import com.betterlifeapps.chessclock.common.Constants.KEY_VIBRATION_GAME_OVER
import com.betterlifeapps.std.BaseViewModel
import com.betterlifeapps.std.components.settings.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settings: Settings) : BaseViewModel() {
    val soundTurn = MutableStateFlow(settings.getBool(KEY_SOUND_TURN, DEFAULT_SOUND_TURN))
    val soundGameOver = MutableStateFlow(settings.getBool(KEY_SOUND_GAME_OVER, DEFAULT_SOUND_GAME_OVER))
    val vibrationGameOver = MutableStateFlow(settings.getBool(KEY_VIBRATION_GAME_OVER, DEFAULT_VIBRATION_GAME_OVER))

    fun onSoundTurnCheckedChanged(value: Boolean) {
        settings.setBool(KEY_SOUND_TURN, value)
        soundTurn.value = value
    }

    fun onSoundGameOverCheckedChanged(value: Boolean) {
        settings.setBool(KEY_SOUND_GAME_OVER, value)
        soundGameOver.value = value
    }

    fun onVibrationGameOverCheckedChanged(value: Boolean) {
        settings.setBool(KEY_VIBRATION_GAME_OVER, value)
        vibrationGameOver.value = value
    }
}