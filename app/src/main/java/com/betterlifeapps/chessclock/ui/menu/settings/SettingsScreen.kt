package com.betterlifeapps.chessclock.ui.menu.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.std.ui.composables.UiSwitchSetting

@Composable
fun SettingsScreen() {
    val viewModel = hiltViewModel<SettingsViewModel>()
    Column(Modifier.fillMaxSize()) {

        UiSwitchSetting(
            text = stringResource(id = R.string.setting_sound_turn),
            checked = viewModel.soundTurn.collectAsState().value,
            onCheckedChange = viewModel::onSoundTurnCheckedChanged
        )
        UiSwitchSetting(
            text = stringResource(id = R.string.setting_sound_game_over),
            checked = viewModel.soundGameOver.collectAsState().value,
            onCheckedChange = viewModel::onSoundGameOverCheckedChanged
        )
        UiSwitchSetting(
            text = stringResource(id = R.string.setting_vibration),
            checked = viewModel.vibrationGameOver.collectAsState().value,
            onCheckedChange = viewModel::onVibrationGameOverCheckedChanged
        )
    }
}