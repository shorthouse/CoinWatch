package dev.shorthouse.coinwatch.navigation

sealed class Graph(val route: String) {
    object Root : Screen("root_graph")
    object Home : Screen("home_graph")
    object Details : Screen("details_graph")
}
