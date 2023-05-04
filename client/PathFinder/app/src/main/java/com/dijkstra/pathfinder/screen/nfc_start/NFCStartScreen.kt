package com.dijkstra.pathfinder.screen.nfc_start

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.dijkstra.pathfinder.navigation.Screen

private const val TAG = "NFCStartScreen_싸피"

@Composable
fun NFCStartScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navController: NavController,
    nfcViewModel: NFCViewModel = hiltViewModel<NFCViewModel>(LocalContext.current as ComponentActivity)
) {
    val nfcState by nfcViewModel.nfcState.collectAsState()
    val nfcSharedState by nfcViewModel.sharedNFCStateFlow.collectAsState("")
    
//    nfcSharedState.let {
//        NFCStartContent()
//
//        LaunchedEffect(key1 = nfcSharedState) {
//            if (nfcSharedState == "NEW NFC DATA") {
//                navController.navigate(route = Screen.Test.route)
//            } else if (nfcSharedState == "SECOND") {
//                navController.navigate(route = Screen.Test2.route)
//            }
//        }
//    }

    nfcState.let {
        NFCStartContent(nfcData = nfcState)

        LaunchedEffect(key1 = nfcState) {
            if (nfcState == "NEW NFC DATA") {
                navController.navigate(route = Screen.Test.route)
            } else if (nfcState == "SECOND") {
                navController.navigate(route = Screen.Test2.route)
            }
        }
    }
} // End of NFCStartScreen

// StateHoisting
@Composable
fun NFCStartContent(nfcData: String = "") {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "NFC 태그를 해주세요")
                Spacer(modifier = Modifier.padding(40.dp))
                CircularProgressIndicator()
            }
        }
    }
} // End of NFCStartContent

@Preview
@Composable
private fun NFCStartPreview() {
    Surface(modifier = Modifier.background(color = Color.White)) {
        NFCStartContent()
    }
} // End of NFCStartPreview