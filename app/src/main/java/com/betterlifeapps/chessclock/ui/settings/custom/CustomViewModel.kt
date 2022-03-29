package com.betterlifeapps.chessclock.ui.settings.custom

import com.betterlifeapps.chessclock.data.GameModeRepository
import com.betterlifeapps.std.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class CustomViewModel @Inject constructor(gameModeRepository: GameModeRepository) :
    BaseViewModel() {

    fun deleteTimeControl(id: Int) {
        // TODO
    }

    val timeControls: Flow<List<ItemCustomTimeControl>> =
        gameModeRepository.getCustomGameModes().map {
            it.map {
                ItemCustomTimeControl(
                    it.id, it.name, formatter.format(
                        it.createDate.withOffsetSameInstant(
                            ZoneOffset.from(OffsetDateTime.now())
                        )
                    )
                )
            }
        }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
    }
}