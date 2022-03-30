package com.betterlifeapps.chessclock.ui.game

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.common.DialogManager
import com.betterlifeapps.chessclock.databinding.FragmentGameBinding
import com.betterlifeapps.chessclock.domain.GameState
import com.betterlifeapps.chessclock.ui.widget.PlayerView
import com.betterlifeapps.std.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GameFragment : BaseFragment(R.layout.fragment_game) {

    private val binding by viewBinding<FragmentGameBinding>()
    private val viewModel by viewModels<GameViewModel>()

    @Inject
    lateinit var dialogManager: DialogManager

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
                R.color.white
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
            viewModel.onRestartClicked()
        }

        viewModel.gameState
            .onEach(::bindGameState)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.uiEvent
            .onEach(::onUiEvent)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun bindGameState(gameState: GameState) = with(binding) {
        playerView1.bindState(gameState.player1)
        playerView2.bindState(gameState.player2)
        controlButton.setImageResource(if (gameState.isPaused) R.drawable.ic_play_48 else R.drawable.ic_pause_48)
        settings.isVisible = gameState.isPaused
        restart.isVisible = gameState.isPaused

        if (!gameState.isPaused) {
            if (gameState.isFirstPlayerTurn) {
                startHeightAnim(playerView1, playerView2)
            } else {
                startHeightAnim(playerView2, playerView1)
            }
        }
    }

    private fun startHeightAnim(targetView: PlayerView, secondaryView: PlayerView) {
        val startHeight = targetView.measuredHeight
        val endHeight = (binding.container.measuredHeight * EXPANDED_RATIO).toInt()
        val valueAnimator = ValueAnimator.ofInt(
            startHeight,
            endHeight
        ).apply {
            duration = ANIM_DURATION
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val animatedValue = it.animatedValue as Int
                targetView.updateLayoutParams {
                    height = animatedValue
                }
                secondaryView.updateLayoutParams {
                    height = binding.container.measuredHeight - animatedValue
                }
            }
        }
        valueAnimator.start()
    }

    private fun resetPlayerViewsHeight() {
        binding.playerView1.updateLayoutParams {
            height = binding.container.measuredHeight / 2
        }
        binding.playerView2.updateLayoutParams {
            height = binding.container.measuredHeight / 2
        }
    }

    private fun onUiEvent(event: GameViewModel.UiEvent) {
        when (event) {
            is GameViewModel.UiEvent.TimeExpired -> {
                //TODO Make proper loss message
                val player = if (event.isFirstPlayerTurn) "First" else "Second"
                Toast.makeText(requireContext(), "$player player time expired!", Toast.LENGTH_LONG)
                    .show()
            }
            is GameViewModel.UiEvent.ShowConfirmationDialog -> {
                dialogManager.showConfirmationDialog(
                    getString(R.string.restart_dialog_title),
                    getString(R.string.restart_dialog_message)
                ) {
                    event.onConfirmClicked()
                }
            }
            is GameViewModel.UiEvent.Restart -> {
                resetPlayerViewsHeight()
            }
        }
    }

    companion object {
        private const val ANIM_DURATION = 200L
        private const val EXPANDED_RATIO = 0.8f
    }
}