package com.dijkstra.pathfinder

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.dijkstra.pathfinder.ui.theme.PathFinderTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val intent = Intent(this, UnityHolderActivity::class.java)
            PathFinderTheme {

                val btPermissionsState = rememberMultiplePermissionsState(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        listOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.CAMERA
                        )
                    } else {
                        listOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.CAMERA
                        )
                    }
                ) // End of btPermissionsState

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp),
                            onClick = {
                                for (i in btPermissionsState.permissions.indices) {
                                    Log.d(
                                        "SSAFY_PERMISSION",
                                        "${btPermissionsState.permissions[i]}: ${btPermissionsState.permissions[i].status}"
                                    )
                                }

                                if (btPermissionsState.allPermissionsGranted) {
                                    startActivity(intent)
                                } else {
                                    btPermissionsState.launchMultiplePermissionRequest()
                                }
                            }) {
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
//    Text(text = "Hello ${BuildConfig.NAVER_CLIENT_ID}!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PathFinderTheme {
        Greeting("Android")
    }
}