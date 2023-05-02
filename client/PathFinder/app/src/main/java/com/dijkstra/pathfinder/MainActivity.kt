package com.dijkstra.pathfinder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dijkstra.pathfinder.navigation.AppNavigation
import com.dijkstra.pathfinder.screen.main.MainScreen
import com.dijkstra.pathfinder.ui.theme.PathFinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intent = Intent(this, UnityHolderActivity::class.java)
            PathFinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Button(
//                            modifier = Modifier.width(100.dp).height(50.dp),
//                            onClick = { startActivity(intent) }) {
//                        }
//                    }
                    MainScreen()
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