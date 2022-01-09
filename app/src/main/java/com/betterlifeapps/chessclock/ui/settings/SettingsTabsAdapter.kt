package com.betterlifeapps.chessclock.ui.settings

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.ui.settings.custom.CustomFragment
import com.betterlifeapps.chessclock.ui.settings.standard.StandardFragment

class SettingsTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        StandardFragment.newInstance(),
        CustomFragment.newInstance()
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = fragments[position]
}