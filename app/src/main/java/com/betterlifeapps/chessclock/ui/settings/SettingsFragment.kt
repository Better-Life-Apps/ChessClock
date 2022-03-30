package com.betterlifeapps.chessclock.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.FragmentSettingsBinding
import com.betterlifeapps.std.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding<FragmentSettingsBinding>()
    private lateinit var settingsTabsAdapter: SettingsTabsAdapter
    private val titles = listOf(R.string.standard, R.string.custom)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = getString(R.string.select_time_control)
        binding.toolbar.onBackButtonClicked = {
            findNavController().navigateUp()
        }

        setupTabs()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupTabs() = with(binding) {
        //TabsAdapter should be lateinit, not lazy to avoid crash
        settingsTabsAdapter = SettingsTabsAdapter(requireActivity())
        binding.viewPager.adapter = settingsTabsAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(titles[position])
        }.attach()
    }
}