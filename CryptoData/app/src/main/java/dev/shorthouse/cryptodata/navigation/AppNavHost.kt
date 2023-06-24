package dev.shorthouse.cryptodata.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.shorthouse.cryptodata.ui.screen.Screen
import dev.shorthouse.cryptodata.ui.screen.detail.DetailScreen
import dev.shorthouse.cryptodata.ui.screen.list.ListScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.ListScreen.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.ListScreen.route) {
            ListScreen(navController = navController)
        }
        composable(route = Screen.DetailScreen.route + "/{coinId}") {
            DetailScreen(navController = navController)
        }
    }
}
