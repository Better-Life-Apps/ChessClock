package com.betterlifeapps.chessclock.ui.menu.modes.standard

data class ItemStandardGameMode(
    val id: Int,
    val isSelected: Boolean,
    val title: String,
    val timeDescription: String = "",
    val additionDescription: String = ""
)
