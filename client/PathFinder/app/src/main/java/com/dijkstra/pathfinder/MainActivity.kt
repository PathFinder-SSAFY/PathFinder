package com.dijkstra.pathfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dijkstra.pathfinder.screen.login.LoginScreen
import com.dijkstra.pathfinder.screen.login.LoginViewModel
import com.dijkstra.pathfinder.screen.splash.SplashScreen
import com.dijkstra.pathfinder.ui.theme.PathFinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PathFinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    val loginViewModel = LoginViewModel()
//                    LoginScreen(loginViewModel)
                    SplashScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello ${BuildConfig.NAVER_CLIENT_ID}!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PathFinderTheme {
        Greeting("Android")
    }
}