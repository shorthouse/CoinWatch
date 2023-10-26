package dev.shorthouse.coinwatch.navigation

import androidx.compose.foundation.layout.RowScope
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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dev.shorthouse.coinwatch.ui.screen.details.CoinDetailsScreen
import dev.shorthouse.coinwatch.ui.screen.favourites.FavouritesScreen
import dev.shorthouse.coinwatch.ui.screen.list.ListScreen
import dev.shorthouse.coinwatch.ui.screen.search.SearchScreen
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        route = Graph.Root.route,
        startDestination = Graph.Home.route
    ) {
        composable(route = Graph.Home.route) {
            Scaffold(
                bottomBar = { AppNavigationBar(navController = navController) },
                content = { scaffoldPadding ->
                    HomeNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(scaffoldPadding)
                    )
                },
                modifier = modifier
            )
        }
    }
}

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        route = Graph.Home.route,
        startDestination = NavigationBarScreen.Market.route,
        modifier = modifier
    ) {
        composable(route = NavigationBarScreen.Market.route) {
            ListScreen(navController = navController)
        }
        composable(route = NavigationBarScreen.Favourites.route) {
            FavouritesScreen(navController = navController)
        }
        composable(route = NavigationBarScreen.Search.route) {
            SearchScreen(navController = navController)
        }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.Details.route,
        startDestination = Screen.Details.route
    ) {
        composable(route = Screen.Details.route + "/{coinId}") {
            CoinDetailsScreen(navController = navController)
        }
    }
}

@Composable
fun AppNavigationBar(navController: NavHostController) {
    val navigationBarScreens = remember {
        persistentListOf(
            NavigationBarScreen.Market,
            NavigationBarScreen.Favourites,
            NavigationBarScreen.Search
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showNavigationBar = navigationBarScreens.any { it.route == currentDestination?.route }

    if (showNavigationBar) {
        NavigationBar {
            navigationBarScreens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: NavigationBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val selected = currentDestination?.hierarchy?.any { destination ->
        destination.route == screen.route
    } == true

    NavigationBarItem(
        label = {
            Text(text = stringResource(screen.nameResourceId))
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = null
            )
        },
        selected = selected,
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
        ),
        modifier = modifier
    )
}
