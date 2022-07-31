package com.betterlifeapps.chessclock.ui.menu.modes.custom

interface ListItemClickListener {
    fun onItemClicked(item: ItemCustomGameMode) = Unit
    fun onItemLongClicked(item: ItemCustomGameMode) = Unit
}