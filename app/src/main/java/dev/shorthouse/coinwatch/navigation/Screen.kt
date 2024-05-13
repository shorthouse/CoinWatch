package dev.shorthouse.coinwatch.navigation

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R

sealed class NavigationBarScreen(
    val route: String,
    @StringRes val nameResourceId: Int
) {
    data object Market : NavigationBarScreen(
        route = "market_screen",
        nameResourceId = R.string.market_screen
    )

    data object Favourites : NavigationBarScreen(
        route = "favourites_screen",
        nameResourceId = R.string.favourites_screen
    )

    data object Search : NavigationBarScreen(
        route = "search_screen",
        nameResourceId = R.string.search_screen
    )
}

sealed class Screen(val route: String) {
    data object Details : Screen("details_screen")
    data object Settings : Screen("settings_screen")
    data object NavigationBar : Screen("navigation_bar_screen")
}
