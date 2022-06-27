package com.betterlifeapps.chessclock.ui.settings

import android.content.Context
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.common.DialogManager
import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.chessclock.data.GameStateRepository
import com.betterlifeapps.chessclock.domain.State
import com.betterlifeapps.std.BaseViewModel
import com.betterlifeapps.std.ResourceResolver
import com.betterlifeapps.std.common.DialogUtil
import com.betterlifeapps.std.common.UiEvent

/**
 * This ViewModel is designed to be the superclass of StandardViewModel and CustomViewModel
 * to reuse some logic
 */
abstract class GameModeListViewModel constructor(
    private val gameModeRepository: GameModeRepository,
    private val gameStateRepository: GameStateRepository,
    private val resourceResolver: ResourceResolver
) : BaseViewModel() {
    fun onGameModeSelected(id: Int, context: Context) {
        runCoroutine {
            val selectedGameMode = gameModeRepository.getSelectedGameModeSync()
            // Do nothing if the game mode is already selected
            if (id != selectedGameMode?.id) {
                val gameState = gameStateRepository.getGameState()

                // Select another game mode if the game isn't played, show confirmation dialog otherwise
                if (gameState?.state == State.READY || gameState?.state == State.FINISHED) {
                    selectGameMode(id)
                } else {
                    DialogUtil.showConfirmationDialog(
                        resourceResolver.getString(R.string.select_game_mode_confirmation_title),
                        resourceResolver.getString(R.string.select_game_mode_confirmation_message),
                        context
                    ) {
                        runCoroutine {
                            selectGameMode(id)
                        }
                    }
                }
            }
        }
    }

    private suspend fun selectGameMode(id: Int) {
        gameModeRepository.selectGameMode(id)
        postUiEvent(UiEvent.ShowShortToastRes(R.string.game_mode_updated))
    }
}