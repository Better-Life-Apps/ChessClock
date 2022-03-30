package com.betterlifeapps.chessclock.ui.settings.standard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.FragmentStandardBinding
import com.betterlifeapps.std.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StandardFragment : BaseFragment(R.layout.fragment_standard) {

    private val binding: FragmentStandardBinding by viewBinding()
    private val viewModel: StandardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = StandardListAdapter(viewModel::onItemClicked)
        binding.recyclerViewStandard.adapter = adapter

        viewModel.standardTimeControls
            .onEach { adapter.submitList(it) }
            .launchIn(lifecycleScope)
    }

    companion object {
        fun newInstance() = StandardFragment()
    }
}