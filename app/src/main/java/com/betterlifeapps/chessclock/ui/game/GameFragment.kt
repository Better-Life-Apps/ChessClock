package com.betterlifeapps.chessclock.ui.game

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.FragmentGameBinding
import com.betterlifeapps.chessclock.domain.GameState
import com.betterlifeapps.std.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GameFragment : BaseFragment(R.layout.fragment_game) {

    private val binding by viewBinding<FragmentGameBinding>()
    private val viewModel by viewModels<GameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportActionBar?.hide()

        binding.playerView1.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow
            )
        )
        binding.playerView2.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow
            )
        )

        binding.playerView1.setOnClickListener {
            viewModel.onPlayer1Clicked()
        }

        binding.playerView2.setOnClickListener {
            viewModel.onPlayer2Clicked()
        }

        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.dest_settings)
        }

        binding.controlButton.setOnClickListener {
            viewModel.onControlButtonClicked()
        }

        binding.restart.setOnClickListener {
            //TODO Add confirmation dialog
            viewModel.onRestartClicked()
        }

        viewModel.gameState
            .onEach(::bindGameState)
            .launchIn(lifecycleScope)

        viewModel.uiEvent
            .onEach(::onUiEvent)
            .launchIn(lifecycleScope)
    }

    private fun bindGameState(gameState: GameState) = with(binding) {
        playerView1.bindState(gameState.player1)
        playerView2.bindState(gameState.player2)
        controlButton.setImageResource(if (gameState.isPaused) R.drawable.ic_play_48 else R.drawable.ic_pause_48)
        settings.isVisible = gameState.isPaused
        restart.isVisible = gameState.isPaused
    }

    private fun onUiEvent(event: GameViewModel.UiEvent) {
        when (event) {
            is GameViewModel.UiEvent.TimeExpired -> {
                //TODO Make proper loss message
                val player = if (event.isFirstPlayerTurn) "First" else "Second"
                Toast.makeText(requireContext(), "$player player time expired!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}