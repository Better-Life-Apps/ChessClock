package com.betterlifeapps.chessclock.ui.menu.modes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.betterlifeapps.chessclock.ui.menu.modes.custom.CustomFragment
import com.betterlifeapps.chessclock.ui.menu.modes.standard.StandardFragment

class ModesTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        StandardFragment.newInstance(),
        CustomFragment.newInstance()
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = fragments[position]
}