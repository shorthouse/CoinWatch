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

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val onNavigateDetails: (String) -> Unit = { coinId ->
        navController.navigate(Screen.Details.route + "/$coinId")
    }

    NavHost(
        navController = navController,
        startDestination = Screen.NavigationBar.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = Screen.NavigationBar.route) {
            NavigationBarScaffold(onNavigateDetails = onNavigateDetails)
        }
        composable(
            route = Screen.Details.route + "/{coinId}",
            enterTransition = { fadeIn(animationSpec = tween(700)) },
            exitTransition = { fadeOut(animationSpec = tween(700)) }
        ) {
            DetailsScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
