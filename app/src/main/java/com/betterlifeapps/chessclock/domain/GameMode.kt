package com.betterlifeapps.chessclock.domain

import java.time.OffsetDateTime

data class GameMode(
    val id: Int,
    val isStandard: Boolean,
    val name: String,
    val isSelected: Boolean,
    val createDate: OffsetDateTime,
    val player1TimeControl: TimeControl,
    val player2TimeControl: TimeControl
)
