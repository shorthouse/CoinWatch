package dev.shorthouse.cryptodata.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.shorthouse.cryptodata.ui.screen.Screen
import dev.shorthouse.cryptodata.ui.screen.detail.CoinDetailScreen
import dev.shorthouse.cryptodata.ui.screen.list.CoinListScreen
import timber.log.Timber

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    startDestination: String = Screen.ListScreen.route
) {
    val primary = MaterialTheme.colorScheme.primary
    val background = MaterialTheme.colorScheme.background

    navController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.route) {
            Screen.ListScreen.route -> {
                systemUiController.setSystemBarsColor(color = primary)
            }
            Screen.DetailScreen.route + "/{coinId}" -> {
                systemUiController.setSystemBarsColor(color = background)
            }
            else -> {
                Timber.e("Unknown route: ${destination.route}")
            }
        }
    }

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
