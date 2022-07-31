package com.betterlifeapps.chessclock.ui.game

import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.common.Constants
import com.betterlifeapps.chessclock.common.DialogManager
import com.betterlifeapps.chessclock.databinding.FragmentGameBinding
import com.betterlifeapps.chessclock.domain.GameState
import com.betterlifeapps.chessclock.domain.State
import com.betterlifeapps.chessclock.ui.game.GameViewModel.GameScreenUiEvent
import com.betterlifeapps.chessclock.ui.widget.PlayerView
import com.betterlifeapps.std.BaseFragment
import com.betterlifeapps.std.common.UiEvent
import com.betterlifeapps.std.components.rating.RatingDialogFragment
import com.betterlifeapps.std.components.settings.Settings
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

    @Inject
    lateinit var settings: Settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportActionBar?.hide()

        binding.playerView1.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.yellow
            )
        )
        binding.playerView1.setPlayerLabel(R.string.player_1)
        binding.playerView2.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red_1
            )
        )
        binding.playerView2.setPlayerLabel(R.string.player_2)

        binding.playerView1.setOnClickListener {
            viewModel.onPlayer1Clicked()
        }

        binding.playerView2.setOnClickListener {
            viewModel.onPlayer2Clicked()
        }

        binding.menu.setOnClickListener {
            findNavController().navigate(R.id.dest_menu)
        }

        binding.controlButton.setOnClickListener {
            viewModel.onControlButtonClicked()
        }

        binding.restart.setOnClickListener {
            viewModel.onRestartClicked()
        }

        viewModel.gameState.collectFlow {
            bindGameState(it)
        }

        viewModel.uiEvents
            .onEach(::onUiEvent)
            .launchIn(viewLifecycleOwner.lifecycleScope)
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
        menu.isVisible = gameState.state != State.RUNNING
        restart.isVisible = gameState.state == State.PAUSED

        when (gameState.state) {
            State.READY -> {
                resetPlayerViewsHeight()
            }
            State.RUNNING -> {
                if (gameState.isFirstPlayerTurn) {
                    animateHeightChange(playerView1, playerView2)
                } else {
                    animateHeightChange(playerView2, playerView1)
                }
            }
            State.PAUSED, State.FINISHED -> {
                if (gameState.isFirstPlayerTurn) {
                    updatePlayersHeight(playerView1, playerView2)
                } else {
                    updatePlayersHeight(playerView2, playerView1)
                }
            }
        }

        // Show player labels only if game state is READY
        binding.playerView1.setPlayerLabelVisible(gameState.state == State.READY)
        binding.playerView2.setPlayerLabelVisible(gameState.state == State.READY)
    }

    private fun updatePlayersHeight(bigger: PlayerView, smaller: PlayerView) {
        view?.doOnLayout {
            val totalHeight = getTotalHeight()
            bigger.updateLayoutParams {
                height = (totalHeight * EXPANDED_RATIO).toInt()
            }
            smaller.updateLayoutParams {
                height = (totalHeight * (1 - EXPANDED_RATIO)).toInt()
            }
        }
    }

    private fun animateHeightChange(targetView: PlayerView, secondaryView: PlayerView) {
        val totalHeight = getTotalHeight()
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

    private fun getTotalHeight() = try {
        // Potential workaround for IllegalStateException in binding
        binding.container.measuredHeight
    } catch (e: IllegalStateException) {
        e.printStackTrace()
        0
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
        val shouldShowRatingDialog = shouldShowRatingDialog()
        val textMessage = if (event.isFirstPlayerTurn) {
            R.string.first_player_time_expired
        } else {
            R.string.second_player_time_expired
        }
        // Don't show toast if rating dialog should be shown
        if (!shouldShowRatingDialog) {
            showLongToast(textMessage)
        }

        val vibrator =
            ContextCompat.getSystemService(requireContext(), Vibrator::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(300L, 200)
            vibrator?.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(300L)
        }

        if (shouldShowRatingDialog) {
            showRatingDialog()
        }
    }

    private fun shouldShowRatingDialog(): Boolean {
        val launchCount = settings.getInt(Settings.KEY_LAUNCH_COUNT, 0)
        val gamesToNextRatingDialog = settings.getInt(Constants.KEY_GAMES_TO_NEXT_RATING_DIALOG, 3)
        settings.setInt(Constants.KEY_GAMES_TO_NEXT_RATING_DIALOG, gamesToNextRatingDialog - 1)
        val ratingSubmitted = settings.getBool(Settings.KEY_RATING_SUBMITTED, false)

        return launchCount > 2 && gamesToNextRatingDialog <= 0 && !ratingSubmitted
    }

    private fun showRatingDialog() {
        observeFragmentResult<RatingDialogFragment.RatingDialogResult>(RatingDialogFragment.TAG_RATING_DIALOG_RESULT) {
            if (it == RatingDialogFragment.RatingDialogResult.DISMISSED) {
                settings.setInt(Constants.KEY_GAMES_TO_NEXT_RATING_DIALOG, 3)
            }
        }
        if (!parentFragmentManager.isStateSaved) {
            RatingDialogFragment().show(parentFragmentManager, null)
        }
    }

    companion object {
        private const val ANIM_DURATION = 200L
        private const val EXPANDED_RATIO = 0.8f
    }
}