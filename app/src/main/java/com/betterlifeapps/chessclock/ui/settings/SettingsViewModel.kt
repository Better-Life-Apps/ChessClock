package com.betterlifeapps.chessclock.ui.settings

import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.std.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    gameModeRepository: GameModeRepository
) : BaseViewModel() {

    val isStandardTabSelected = MutableStateFlow<Boolean?>(null)

    init {
        gameModeRepository.getSelectedGameMode().collectFlow {
            isStandardTabSelected.value = it.isStandard
        }
    }
}