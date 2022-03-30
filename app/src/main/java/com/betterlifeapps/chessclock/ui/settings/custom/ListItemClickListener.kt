package com.betterlifeapps.chessclock.ui.settings.custom

import com.betterlifeapps.chessclock.domain.GameMode

interface ListItemClickListener {
    fun onItemClicked(item: ItemCustomGameMode) = Unit
    fun onItemLongClicked(item: ItemCustomGameMode) = Unit
}