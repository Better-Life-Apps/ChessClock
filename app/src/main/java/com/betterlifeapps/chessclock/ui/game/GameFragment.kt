package com.betterlifeapps.chessclock.ui.game

import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commitNow
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.common.DialogManager
import com.betterlifeapps.chessclock.databinding.FragmentGameBinding
import com.betterlifeapps.chessclock.domain.GameState
import com.betterlifeapps.chessclock.domain.State
import com.betterlifeapps.chessclock.ui.game.GameViewModel.GameScreenUiEvent
import com.betterlifeapps.chessclock.ui.widget.PlayerView
import com.betterlifeapps.std.BaseFragment
import com.betterlifeapps.std.common.UiEvent
import com.betterlifeapps.std.components.rating.RatingDialogFragment
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
                R.color.red_1
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

        viewModel.uiEvents
            .onEach(::onUiEvent)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        RatingDialogFragment().show(parentFragmentManager, null)
    }

    private fun bindGameState(gameState: GameState) = with(binding) {
        playerView1.bindState(gameState.player1)
        playerView2.bindState(gameState.player2)
        controlButton.setImageResource(
            when (gameState.state) {
                State.RUNNING -> R.drawable.ic_pause_48
                State.FINISHED -> R.drawable.ic_restart_48
                else -> R.drawable.ic_play_48
            }
        )
        settings.isVisible = gameState.state != State.RUNNING
        restart.isVisible = gameState.state == State.PAUSED

        if (gameState.state == State.RUNNING) {
            if (gameState.isFirstPlayerTurn) {
                startHeightAnim(playerView1, playerView2)
            } else {
                startHeightAnim(playerView2, playerView1)
            }
        }
        if (gameState.state == State.READY) {
            resetPlayerViewsHeight()
        }
    }

    private fun startHeightAnim(targetView: PlayerView, secondaryView: PlayerView) {
        // Potential workaround for IllegalStateException in binding
        val totalHeight = try {
            binding.container.measuredHeight
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            0
        }
        val startHeight = targetView.measuredHeight
        val endHeight = (totalHeight * EXPANDED_RATIO).toInt()
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
                    height = totalHeight - animatedValue
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

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is GameScreenUiEvent.TimeExpired -> {
                onTimeExpired(event)
            }
            is GameScreenUiEvent.ShowConfirmationDialog -> {
                dialogManager.showConfirmationDialog(
                    getString(R.string.restart_dialog_title),
                    getString(R.string.restart_dialog_message)
                ) {
                    event.onConfirmClicked()
                }
            }
            is GameScreenUiEvent.Restart -> {
                resetPlayerViewsHeight()
            }
            else -> super.onUiEvent(event)
        }
    }

    private fun onTimeExpired(event: GameScreenUiEvent.TimeExpired) {
        val textMessage = if (event.isFirstPlayerTurn) {
            R.string.first_player_time_expired
        } else {
            R.string.second_player_time_expired
        }
        showLongToast(textMessage)

        val vibrator =
            ContextCompat.getSystemService(requireContext(), Vibrator::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(300L, 200)
            vibrator?.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(300L)
        }
    }

    companion object {
        private const val ANIM_DURATION = 200L
        private const val EXPANDED_RATIO = 0.8f
    }
}