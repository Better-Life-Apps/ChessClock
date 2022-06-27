package com.betterlifeapps.chessclock.ui.settings.custom

import android.content.Context
import com.betterlifeapps.chessclock.common.DialogManager
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.data.GameStateRepository
import com.betterlifeapps.chessclock.ui.settings.GameModeListViewModel
import com.betterlifeapps.std.ResourceResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class CustomViewModel @Inject constructor(
    private val gameModeRepository: GameModeRepository,
    gameStateRepository: GameStateRepository,
    resourceResolver: ResourceResolver
) : GameModeListViewModel(
    gameModeRepository,
    gameStateRepository,
    resourceResolver
) {

    fun deleteGameMode(item: ItemCustomGameMode) {
        runCoroutine {
            gameModeRepository.deleteAndCheckSelection(item.id, item.isSelected)
        }
    }

    fun selectGameMode(item: ItemCustomGameMode, context: Context) {
        onGameModeSelected(item.id, context)
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