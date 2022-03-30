package com.betterlifeapps.chessclock.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AbstractComposeView
import com.betterlifeapps.std.ui.composables.UiToolbar
import com.betterlifeapps.std.ui.theme.UiTheme

class UiToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    var title by mutableStateOf("")
    var onBackButtonClicked: (() -> Unit)? = null

    @Composable
    override fun Content() {
        UiTheme {
            UiToolbar(
                text = title,
                onBackButtonClick = { onBackButtonClicked?.invoke() }
            )
        }
    }
}