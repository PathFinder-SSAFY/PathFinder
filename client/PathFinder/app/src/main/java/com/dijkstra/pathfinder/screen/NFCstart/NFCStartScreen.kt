package com.dijkstra.pathfinder.screen.NFCstart

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dijkstra.pathfinder.navigation.Screen

private const val TAG = "NFCStartScreen_싸피"

@Composable
fun NFCStartScreen(
    navController: NavController,
    nfcViewModel: NFCViewModel = viewModel(LocalContext.current as ComponentActivity)
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