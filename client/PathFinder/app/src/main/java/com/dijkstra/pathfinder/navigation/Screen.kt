package com.dijkstra.pathfinder.navigation

sealed class Screen(val route: String) {
    object Splash : Screen(route = "splash_screen")
    object NFCStart : Screen(route = "NFCstart_screen")
    object Main : Screen(route = "main_screen")
    object Login : Screen(route = "login_screen")
    object Test : Screen(route = "test_screen")
    object Test2 : Screen(route = "test2_screen")
} // End of Screen class
