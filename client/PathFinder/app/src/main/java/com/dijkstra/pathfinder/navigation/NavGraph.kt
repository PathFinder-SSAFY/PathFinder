package com.dijkstra.pathfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dijkstra.pathfinder.screen.TestScreen
import com.dijkstra.pathfinder.screen.main.MainScreen
import com.dijkstra.pathfinder.screen.nfc_start.NFCStartScreen
import com.dijkstra.pathfinder.screen.splash.SplashScreen
import com.dijkstra.pathfinder.screen.test.TestScreen2


@Composable
fun SetUpNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Splash.route
        ) {
            SplashScreen(navController = navController)
        }
        composable(
            route = Screen.NFCStart.route
        ) {
            NFCStartScreen(navController = navController)
        }
        composable(
            route = Screen.Main.route
        ) {
            MainScreen(navController = navController)
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
        composable(
            route = Screen.Test2.route
        ) {
            TestScreen2(navController = navController)
        }
    }
} // End of setUpNavGraph