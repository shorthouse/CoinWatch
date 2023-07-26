package dev.shorthouse.coinwatch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.shorthouse.coinwatch.ui.screen.Screen
import dev.shorthouse.coinwatch.ui.screen.detail.CoinDetailScreen
import dev.shorthouse.coinwatch.ui.screen.list.CoinListScreen

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
            CoinListScreen(navController = navController)
        }
        composable(route = Screen.DetailScreen.route + "/{coinId}") {
            CoinDetailScreen(navController = navController)
        }
    }
}
