package com.betterlifeapps.chessclock.ui.menu.modes.edit

import androidx.annotation.StringRes
import com.betterlifeapps.chessclock.R

sealed class TimerMode(@StringRes val titleRes: Int) {
    data class ConstantTime(val timePerTurn: String = "00:00") :
        TimerMode(R.string.mode_constant_time)

    data class TimeAddition(val startTime: String = "00:00", val addition: String = "00:00") :
        TimerMode(R.string.mode_time_addition)

    data class NoAddition(val startTime: String = "00:00") : TimerMode(R.string.mode_no_addition)
}