package com.betterlifeapps.chessclock.ui.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import com.betterlifeapps.chessclock.R
import com.betterlifeapps.chessclock.ui.menu.modes.ModesScreen
import com.betterlifeapps.chessclock.ui.menu.settings.SettingsScreen
import com.betterlifeapps.chessclock.ui.theme.ChessAppTheme
import com.betterlifeapps.chessclock.ui.theme.TabDeselected
import com.betterlifeapps.std.BaseComposeFragment
import com.betterlifeapps.std.ui.composables.UiToolbar

class MenuFragment : BaseComposeFragment() {

    @Composable
    override fun View() {
        val navController = rememberNavController()
        ChessAppTheme {
            Column {
                UiToolbar {
                    findNavController().navigateUp()
                }
                NavHost(
                    navController = navController,
                    startDestination = "modes",
                    modifier = Modifier.weight(1f)
                ) {
                    composable("modes") {
                        ModesScreen()
                    }
                    composable("settings") {
                        SettingsScreen()
                    }
                    composable("themes") {}
                    composable("about") {}
                }
                BottomNavigationBar(navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val items = listOf(
        NavigationItem(R.string.bottom_nav_modes, "modes") {
            Icon(painter = painterResource(id = R.drawable.ic_modes), contentDescription = null)
        },
        NavigationItem(R.string.bottom_nav_settings, "settings") {
            Icon(painter = painterResource(id = R.drawable.ic_settings), contentDescription = null)
        },
        NavigationItem(R.string.bottom_nav_themes, "themes") {
            Icon(painter = painterResource(id = R.drawable.ic_themes), contentDescription = null)
        },
        NavigationItem(R.string.bottom_nav_about, "about") {
            Icon(painter = painterResource(id = R.drawable.ic_about), contentDescription = null)
        }
    )
    BottomNavigation(
        backgroundColor = Color.White
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = item.icon,
                label = { Text(text = stringResource(id = item.titleRes)) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = TabDeselected,
                alwaysShowLabel = true,
                selected = item.route == navBackStackEntry?.destination?.route,
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }
    }
}

data class NavigationItem(
    @StringRes val titleRes: Int,
    val route: String,
    val icon: @Composable () -> Unit
)