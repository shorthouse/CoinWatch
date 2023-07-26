package dev.shorthouse.coinwatch.ui.screen

sealed class Screen(val route: String) {
    object ListScreen : Screen("list_screen")
    object DetailScreen : Screen("detail_screen")
}
