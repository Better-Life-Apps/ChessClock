package com.betterlifeapps.chessclock.ui.menu.themes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.std.ui.composables.VSpacer

@Composable
fun ThemesScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_under_development))

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(256.dp),

            )
        VSpacer(height = 8)
        Text(text = stringResource(id = R.string.coming_soon), style = MaterialTheme.typography.h5)
        VSpacer(height = 8)
        Text(
            text = stringResource(id = R.string.themes_coming_soon_message),
            style = MaterialTheme.typography.body2
        )
    }
}