package com.betterlifeapps.chessclock.ui.menu.modes.custom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.common.DialogManager
import com.betterlifeapps.chessclock.databinding.FragmentCustomBinding
import com.betterlifeapps.std.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CustomFragment : BaseFragment(R.layout.fragment_custom) {

    private val viewModel: CustomViewModel by viewModels()
    private val binding: FragmentCustomBinding by viewBinding()

    @Inject
    internal lateinit var dialogManager: DialogManager

    private val customClickListener = object : ListItemClickListener {
        override fun onItemClicked(item: ItemCustomGameMode) {
            viewModel.selectGameMode(item, requireContext())
        }

        override fun onItemLongClicked(item: ItemCustomGameMode) {
            showDeleteTimeControlDialog(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        binding.createNewTimeControl.createNewTimeControlContainer.setOnClickListener {
            onCreateNewClicked()
        }

        viewModel.uiEvents.collectFlow {
            super.onUiEvent(it)
        }
    }

    private fun setupList() {
        val adapter = CustomListAdapter(customClickListener)
        binding.timeControlsRecyclerView.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.timeControlsRecyclerView.addItemDecoration(itemDecoration)

        viewModel.timeControls
            .onEach(adapter::submitList)
            .launchIn(lifecycleScope)
    }

    private fun onCreateNewClicked() {
        findNavController().navigate(R.id.dest_edit)
    }

    private fun showDeleteTimeControlDialog(item: ItemCustomGameMode) {
        val title = getString(R.string.delete_time_control_title)
        val message = getString(R.string.delete_time_control_message)
        dialogManager.showConfirmationDialog(title, message) {
            viewModel.deleteGameMode(item)
        }
    }

    companion object {
        fun newInstance() = CustomFragment()
    }
}