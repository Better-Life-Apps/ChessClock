package com.betterlifeapps.chessclock.ui.settings

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.FragmentSettingsBinding
import com.betterlifeapps.std.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding by viewBinding<FragmentSettingsBinding>()
    private lateinit var settingsTabsAdapter: SettingsTabsAdapter
    private val titles = listOf(R.string.standard, R.string.custom)
    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = getString(R.string.select_time_control)
        binding.toolbar.onBackButtonClicked = {
            findNavController().navigateUp()
        }

        //TabsAdapter should be lateinit, not lazy to avoid crash
        settingsTabsAdapter = SettingsTabsAdapter(requireActivity())
        binding.viewPager.adapter = settingsTabsAdapter

        viewModel.isStandardTabSelected.collectFlow {
            setupTabs(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupTabs(isStandardTabSelected: Boolean?) = with(binding) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position, isStandardTabSelected)
        }.attach()
    }

    private fun getTabTitle(position: Int, isStandardTabSelected: Boolean?): CharSequence {
        return if (isTabDotted(position, isStandardTabSelected)) {
            val string = getString((titles[position])) + "  "
            SpannableStringBuilder(string).apply {
                setSpan(
                    ImageSpan(
                        requireContext(),
                        R.drawable.ic_dot,
                        DynamicDrawableSpan.ALIGN_CENTER
                    ),
                    string.lastIndex,
                    string.lastIndex + 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        } else {
            getString((titles[position]))
        }
    }

    private fun isTabDotted(position: Int, isStandardTabSelected: Boolean?): Boolean {
        return isStandardTabSelected != null && ((position == 0 && isStandardTabSelected) || (position == 1 && !isStandardTabSelected))
    }
}