package com.betterlifeapps.chessclock.ui.settings.custom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CustomViewModel : ViewModel() {

    fun deleteTimeControl(id: Int) {
        // TODO
    }

    val timeControls: Flow<List<ItemCustomTimeControl>> = flowOf(
        listOf(
            ItemCustomTimeControl(
                0,
                "First",
                "15:57 21.12.2021",
                "Total time: 10 min\n Addition: 5 sec"
            ),
            ItemCustomTimeControl(
                1,
                "Second",
                "15:57 21.12.2021",
                "Total time: 10 min\n Addition: 5 sec"
            )
        )
    )
}