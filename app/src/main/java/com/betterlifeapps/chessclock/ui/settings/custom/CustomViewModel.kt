package com.betterlifeapps.chessclock.ui.settings.custom

import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.std.BaseViewModel
import com.betterlifeapps.std.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class CustomViewModel @Inject constructor(private val gameModeRepository: GameModeRepository) :
    BaseViewModel() {

    fun deleteGameMode(item: ItemCustomGameMode) {
        runCoroutine {
            gameModeRepository.deleteAndCheckSelection(item.id, item.isSelected)
        }
    }

    fun selectGameMode(item: ItemCustomGameMode) {
        runCoroutine {
            gameModeRepository.selectGameMode(item.id)
            postUiEvent(UiEvent.ShowShortToastRes(R.string.game_mode_updated))
        }
    }

    val timeControls: Flow<List<ItemCustomGameMode>> =
        gameModeRepository.getCustomGameModes().map { list ->
            list.map {
                ItemCustomGameMode(
                    it.id, it.name, formatter.format(
                        it.createDate.withOffsetSameInstant(
                            ZoneOffset.from(OffsetDateTime.now())
                        )
                    ),
                    it.isSelected
                )
            }
        }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
    }
}