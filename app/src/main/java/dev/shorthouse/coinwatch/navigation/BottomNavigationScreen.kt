package dev.shorthouse.coinwatch.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.shorthouse.coinwatch.ui.screen.details.CoinDetailScreen
import dev.shorthouse.coinwatch.ui.screen.favourites.FavouritesScreen
import dev.shorthouse.coinwatch.ui.screen.list.CoinListScreen
import dev.shorthouse.coinwatch.ui.screen.search.CoinSearchScreen
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BottomNavigationScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val navigationBarScreens = remember {
        persistentListOf(
            NavigationBarScreen.Market,
            NavigationBarScreen.Favourites,
            NavigationBarScreen.Search
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navigationBarScreens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true

                    NavigationBarItem(
                        icon = {
                            if (selected) {
                                Icon(
                                    imageVector = screen.selectedIcon,
                                    contentDescription = null
                                )
                            } else {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = null
                                )
                            }
                        },
                        label = {
                            Text(text = stringResource(screen.nameResourceId))
                        },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.background,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        content = { scaffoldPadding ->
            NavHost(
                navController = navController,
                startDestination = NavigationBarScreen.Market.route,
                modifier = Modifier.padding(scaffoldPadding)
            ) {
                composable(route = NavigationBarScreen.Market.route) {
                    CoinListScreen(navController = navController)
                }
                composable(route = NavigationBarScreen.Favourites.route) {
                    FavouritesScreen(navController = navController)
                }
                composable(route = NavigationBarScreen.Search.route) {
                    CoinSearchScreen(navController = navController)
                }
                composable(route = Screen.CoinDetail.route + "/{coinId}") {
                    CoinDetailScreen(navController = navController)
                }
            }
        },
        modifier = modifier
    )
}
