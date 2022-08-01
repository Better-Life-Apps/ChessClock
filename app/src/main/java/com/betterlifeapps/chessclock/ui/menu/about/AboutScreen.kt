package com.betterlifeapps.chessclock.ui.menu.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.betterlifeapps.chessclock.BuildConfig
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.std.LocalFragmentManager
import com.betterlifeapps.std.components.feedback.FeedbackActivity
import com.betterlifeapps.std.components.rating.RatingDialogFragment
import com.betterlifeapps.std.ui.composables.UiAboutView
import com.betterlifeapps.std.ui.composables.UiMenuItem
import com.betterlifeapps.std.ui.composables.VSpacer
import com.betterlifeapps.std.ui.theme.Grey_Light

@Composable
fun AboutScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        UiAboutView(
            iconDrawableRes = R.drawable.ic_launcher_foreground,
            versionName = BuildConfig.VERSION_NAME
        )

        VSpacer(height = 32)
        val fragmentManager = LocalFragmentManager.current
        val context = LocalContext.current
        Divider(color = Grey_Light, modifier = Modifier.padding(horizontal = 16.dp))
        UiMenuItem(text = stringResource(id = R.string.rate_application)) {
            fragmentManager?.let {
                RatingDialogFragment.newInstance()
                    .show(it, RatingDialogFragment.TAG_RATING_DIALOG_FRAGMENT)
            }
        }
        UiMenuItem(text = stringResource(id = R.string.send_feedback)) {
            FeedbackActivity.start(context)
        }
    }
}