package com.betterlifeapps.chessclock.ui.settings.custom

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CustomFragment : BaseFragment(R.layout.fragment_custom) {

    private val viewModel: CustomViewModel by viewModels()
    private val binding: FragmentCustomBinding by viewBinding()

    @Inject
    internal lateinit var dialogManager: DialogManager

    private val customClickListener = object : ListItemClickListener {
        override fun onItemClicked(itemId: Int) {
            selectTimeControl(itemId)
        }

        override fun onItemLongClicked(itemId: Int) {
            showDeleteTimeControlDialog(itemId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        binding.createNewTimeControl.createNewTimeControlContainer.setOnClickListener {
            onCreateNewClicked()
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
        findNavController().navigate(R.id.action_new_time_control)
    }

    private fun selectTimeControl(id: Int) {
        //TODO
    }

    private fun showDeleteTimeControlDialog(id: Int) {
        val title = getString(R.string.delete_time_control_title)
        val message = getString(R.string.delete_time_control_message)
        dialogManager.showConfirmationDialog(title, message) {
            viewModel.deleteTimeControl(id)
        }
    }

    companion object {
        fun newInstance() = CustomFragment()
    }
}