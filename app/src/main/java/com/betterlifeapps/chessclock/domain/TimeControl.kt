package com.betterlifeapps.chessclock.domain

abstract class TimeControl {
    abstract val startTime: Long
    abstract val additionTime: Long

    data class ConstantTimeControl(val timePerTurn: Long) : TimeControl() {
        override val startTime: Long
            get() = timePerTurn
        override val additionTime: Long
            get() = 0
    }

    data class AdditionTimeControl(override val startTime: Long, override val additionTime: Long) :
        TimeControl()

    data class NoAdditionTimeControl(val totalTime: Long) : TimeControl() {
        override val startTime: Long
            get() = totalTime
        override val additionTime: Long
            get() = 0
    }
}