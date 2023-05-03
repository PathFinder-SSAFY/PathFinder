package com.dijkstra.pathfinder.navigation

sealed class Screen(val route: String) {
    object NFCStart : Screen(route = "NFCstart_screen")
    object Main : Screen(route = "main_screen")
    object Login : Screen(route = "login_screen")
    object Test : Screen(route = "test_screenl")
} // End of Screen class
