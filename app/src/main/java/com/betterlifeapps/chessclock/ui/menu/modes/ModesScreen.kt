package com.betterlifeapps.chessclock.ui.menu.modes

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.betterlifeapps.std.LocalFragmentManager

@Composable
fun ModesScreen() {
    val fragmentManager = LocalFragmentManager.current!!
    FragmentContainer(
        modifier = Modifier.fillMaxSize(),
        fragmentManager = fragmentManager
    )
}

@Composable
fun FragmentContainer(
    modifier: Modifier = Modifier,
    fragmentManager: FragmentManager,
) {

    val containerId by rememberSaveable {
        mutableStateOf(View.generateViewId())
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            FragmentContainerView(context)
                .apply { id = containerId }
        },
        update = { view ->
            fragmentManager.commit {
                add(view.id, ModesFragment.newInstance())
            }
        }
    )
}