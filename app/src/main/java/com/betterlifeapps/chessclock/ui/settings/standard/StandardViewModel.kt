package com.betterlifeapps.chessclock.ui.settings.standard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.flowOf

class StandardViewModel : ViewModel() {
    val standardTimeControls = flowOf(
        listOf(
            ItemStandardTimeControl(0, true, "Rapid", "10 min", "+10 sec"),
            ItemStandardTimeControl(1, false, "Blitz", "3 min", "+2 sec"),
            ItemStandardTimeControl(2, false, "Bullet", "1 min", "+1 sec"),
        )
    )
}