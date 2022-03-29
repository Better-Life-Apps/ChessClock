package com.betterlifeapps.chessclock.domain

sealed class TimeControl {
    data class ConstantTimeControl(val timePerTurn: Long) : TimeControl()
    data class AdditionTimeControl(val startTime: Long, val additionTime: Long) : TimeControl()
    data class NoAdditionTimeControl(val totalTime: Long) : TimeControl()
}