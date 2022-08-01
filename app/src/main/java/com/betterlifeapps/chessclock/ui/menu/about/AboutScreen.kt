package com.betterlifeapps.chessclock.ui.menu.about

import android.text.SpannedString
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.toSpanned
import com.betterlifeapps.chessclock.BuildConfig
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.std.LocalFragmentManager
import com.betterlifeapps.std.components.feedback.FeedbackActivity
import com.betterlifeapps.std.components.rating.RatingDialogFragment
import com.betterlifeapps.std.ext.toAnnotatedString
import com.betterlifeapps.std.ui.composables.UiMenuItem
import com.betterlifeapps.std.ui.composables.VSpacer
import com.betterlifeapps.std.ui.theme.Grey_Light
import com.betterlifeapps.std.ui.theme.Yellow

@Composable
fun AboutScreen() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        VSpacer(height = 32)
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(Yellow)
                .size(108.dp)
        )
        VSpacer(height = 12)
        val version = BuildConfig.VERSION_NAME
        Text(
            text = stringResource(id = R.string.version_template, version),
            style = MaterialTheme.typography.body1
        )
        VSpacer(height = 16)

        val descriptionText = LocalContext.current.getText(R.string.made_with_love)
        val annotatedString = buildAnnotatedString {
            append((descriptionText as SpannedString).toSpanned().toAnnotatedString())
            append(" ")
            appendInlineContent(id = "imageId")
        }

        val inlineContentMap = mapOf(
            "imageId" to InlineTextContent(
                Placeholder(24.sp, 24.sp, PlaceholderVerticalAlign.TextCenter)
            ) {
                Image(
                    painterResource(id = R.drawable.ic_heart),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = ""
                )
            }
        )

        Text(
            text = annotatedString,
            inlineContent = inlineContentMap,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Normal
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