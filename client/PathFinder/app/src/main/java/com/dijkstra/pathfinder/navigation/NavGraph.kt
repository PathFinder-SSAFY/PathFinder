package com.dijkstra.pathfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dijkstra.pathfinder.screen.NFCstart.NFCStartScreen
import com.dijkstra.pathfinder.screen.TestScreen


@Composable
fun SetUpNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NFCStart.route
    ) {
        composable(
            route = Screen.NFCStart.route
        ) {
            NFCStartScreen(navController = navController)
        }
        composable(
            route = Screen.Main.route
        ) {
            // TODO : MainScreen 구현체 넣기
        }
        composable(
            route = Screen.Login.route
        ) {
            // TODO : LoginScreen 구현체 넣기
        }
        composable(
            route = Screen.Test.route
        ) {
            TestScreen(navController = navController)
        }
    }
} // End of setUpNavGraph