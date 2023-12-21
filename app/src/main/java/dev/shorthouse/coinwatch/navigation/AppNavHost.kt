package dev.shorthouse.coinwatch.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.shorthouse.coinwatch.ui.screen.details.DetailsScreen
import dev.shorthouse.coinwatch.ui.screen.settings.SettingsScreen

@Composable
fun AppNavHost(
    navigationBarStartScreen: NavigationBarScreen = NavigationBarScreen.Market,
    navController: NavHostController = rememberNavController()
) {
    val onNavigateDetails: (String) -> Unit = { coinId ->
        navController.navigate(Screen.Details.route + "/$coinId")
    }

    val onNavigateSettings: () -> Unit = {
        navController.navigate(Screen.Settings.route)
    }

    NavHost(
        navController = navController,
        startDestination = Screen.NavigationBar.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = Screen.NavigationBar.route) {
            NavigationBarScaffold(
                startScreen = navigationBarStartScreen,
                onNavigateDetails = onNavigateDetails,
                onNavigateSettings = onNavigateSettings
            )
        }
        composable(
            route = Screen.Details.route + "/{coinId}",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            DetailsScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            SettingsScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
