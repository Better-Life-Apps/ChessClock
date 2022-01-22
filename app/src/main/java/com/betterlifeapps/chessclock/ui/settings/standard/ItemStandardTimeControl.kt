package com.betterlifeapps.chessclock.ui.settings.standard

data class ItemStandardTimeControl(
    val id: Int,
    val isSelected: Boolean,
    val title: String,
    val timeDescription: String = "",
    val additionDescription: String = ""
)
