package com.betterlifeapps.chessclock.ui.settings.custom

interface ListItemClickListener {
    fun onItemClicked(itemId: Int) = Unit
    fun onItemLongClicked(itemId: Int) = Unit
}