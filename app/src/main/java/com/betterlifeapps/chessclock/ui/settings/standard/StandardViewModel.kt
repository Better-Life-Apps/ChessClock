package com.betterlifeapps.chessclock.ui.settings.standard

import android.content.Context
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.data.GameStateRepository
import com.betterlifeapps.chessclock.domain.TimeControl
import com.betterlifeapps.chessclock.ui.settings.GameModeListViewModel
import com.betterlifeapps.std.ResourceResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class StandardViewModel @Inject constructor(
    gameModeRepository: GameModeRepository,
    private val resourceResolver: ResourceResolver,
    gameStateRepository: GameStateRepository
) : GameModeListViewModel(
    gameModeRepository,
    gameStateRepository,
    resourceResolver
) {
    val standardTimeControls = gameModeRepository.getStandardGameModes().map { list ->
        list.map {
            val timeMillis = (it.player1TimeControl as TimeControl.AdditionTimeControl).startTime
            val timeMinutes = timeMillis / 60_000
            val timeDescription =
                resourceResolver.getString(R.string.start_time_pattern, timeMinutes)
            val additionMillis = it.player1TimeControl.additionTime
            val additionSeconds = additionMillis / 1000
            val additionDescription =
                resourceResolver.getString(R.string.addition_time_pattern, additionSeconds)
            ItemStandardGameMode(
                it.id,
                it.isSelected,
                it.name,
                timeDescription,
                additionDescription
            )
        }
    }

    fun onItemClicked(item: ItemStandardGameMode, context: Context) {
        onGameModeSelected(item.id, context)
    }
}