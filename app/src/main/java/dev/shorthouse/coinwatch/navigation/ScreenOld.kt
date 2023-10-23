package dev.shorthouse.coinwatch.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.InsertChart
import androidx.compose.material.icons.rounded.InsertChartOutlined
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.ui.graphics.vector.ImageVector
import dev.shorthouse.coinwatch.R

sealed class ScreenOld(val route: String) {
    object CoinList : ScreenOld("coin_list_screen")
    object CoinDetail : ScreenOld("coin_detail_screen")
    object CoinSearch : ScreenOld("coin_search_screen")
}

sealed class NavigationBarScreen(
    val route: String,
    @StringRes val nameResourceId: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Market : NavigationBarScreen(
        route = "market_screen",
        nameResourceId = R.string.market_screen,
        icon = Icons.Rounded.InsertChartOutlined,
        selectedIcon = Icons.Rounded.InsertChart
    )

    object Favourites : NavigationBarScreen(
        route = "favourites_screen",
        nameResourceId = R.string.favourites_screen,
        icon = Icons.Rounded.StarOutline,
        selectedIcon = Icons.Rounded.Star
    )

    object Search : NavigationBarScreen(
        route = "search_screen",
        nameResourceId = R.string.search_screen,
        icon = Icons.Rounded.Search,
        selectedIcon = Icons.Rounded.Search
    )
}

sealed class Screen(val route: String) {
    object CoinDetail : Screen("detail_screen")
}
