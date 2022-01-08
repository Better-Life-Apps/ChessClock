package com.betterlifeapps.chessclock.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.betterlifeapps.chessclock.databinding.ViewPlayerBinding
import com.betterlifeapps.chessclock.domain.PlayerState
import com.betterlifeapps.chessclock.domain.formattedTime

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewPlayerBinding.inflate(LayoutInflater.from(context), this, true)

    fun bindState(state: PlayerState) {
        binding.time.text = state.formattedTime
        binding.turnNumber.text = state.turn.toString()
    }
}