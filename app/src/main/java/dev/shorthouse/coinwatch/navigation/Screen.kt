package dev.shorthouse.coinwatch.navigation

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R

sealed class NavigationBarScreen(
    val route: String,
    @StringRes val nameResourceId: Int
) {
    object Market : NavigationBarScreen(
        route = "market_screen",
        nameResourceId = R.string.market_screen
    )

    object Favourites : NavigationBarScreen(
        route = "favourites_screen",
        nameResourceId = R.string.favourites_screen
    )

    object Search : NavigationBarScreen(
        route = "search_screen",
        nameResourceId = R.string.search_screen
    )
}

sealed class Screen(val route: String) {
    object Details : Screen("details_screen")
    object Settings : Screen("settings_screen")
    object NavigationBar : Screen("navigation_bar_screen")
}
