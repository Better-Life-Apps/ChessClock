package com.betterlifeapps.chessclock.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.databinding.ViewInputFieldBinding

class InputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewInputFieldBinding.inflate(LayoutInflater.from(context), this, true)

    var hint: String = ""
        set(value) {
            binding.textInputEditText.hint = value
            field = value
        }

    var text: String = ""
        set(value) {
            binding.textInputEditText.setText(value)
            field = value
        }

    init {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.InputFieldView, 0, 0)

        text = ta.getString(R.styleable.InputFieldView_text).orEmpty()
        hint = ta.getString(R.styleable.InputFieldView_hint).orEmpty()
        ta.recycle()
    }
}