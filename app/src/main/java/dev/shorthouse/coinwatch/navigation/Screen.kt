package dev.shorthouse.coinwatch.navigation

sealed class Screen(val route: String) {
    object CoinList : Screen("coin_list_screen")
    object CoinDetail : Screen("coin_detail_screen")
    object CoinSearch : Screen("coin_search_screen")
}
