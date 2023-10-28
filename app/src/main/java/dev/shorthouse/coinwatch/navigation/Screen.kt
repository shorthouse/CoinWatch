package dev.shorthouse.coinwatch.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import dev.shorthouse.coinwatch.R

sealed class NavigationBarScreen(
    val route: String,
    @StringRes val nameResourceId: Int,
    val icon: ImageVector
) {
    object Market : NavigationBarScreen(
        route = "market_screen",
        nameResourceId = R.string.market_screen,
        icon = Icons.Rounded.BarChart
    )

    object Favourites : NavigationBarScreen(
        route = "favourites_screen",
        nameResourceId = R.string.favourites_screen,
        icon = Icons.Rounded.Favorite
    )

    object Search : NavigationBarScreen(
        route = "search_screen",
        nameResourceId = R.string.search_screen,
        icon = Icons.Rounded.Search
    )
}

sealed class Screen(val route: String) {
    object Details : Screen("details_screen")
    object NavigationBar : Screen("navigation_bar_screen")
}
