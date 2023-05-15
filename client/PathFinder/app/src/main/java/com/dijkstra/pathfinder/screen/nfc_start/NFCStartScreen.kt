package com.dijkstra.pathfinder.screen.nfc_start

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.dijkstra.pathfinder.data.dto.NFC
import com.dijkstra.pathfinder.navigation.Screen
import com.dijkstra.pathfinder.ui.theme.nanumSquareNeo
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.R

private const val TAG = "NFCStartScreen_싸피"

@Composable
fun NFCStartScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navController: NavController,
    nfcViewModel: NFCViewModel = hiltViewModel(LocalContext.current as ComponentActivity),
    nfcResponseViewModel: NFCResponseViewModel = hiltViewModel()
) {
    val nfcState = nfcViewModel.nfcState.collectAsState()
    val nfcScreenState = remember { nfcState }

//    val nfcSharedState by nfcViewModel.sharedNFCStateFlow.collectAsState("")

    val postNFCIdResponseSharedFlowState =
        nfcResponseViewModel.postNFCIdResponseSharedFlow.collectAsState(null)

    LaunchedEffect(key1 = postNFCIdResponseSharedFlowState.value) {
        when (postNFCIdResponseSharedFlowState.value) {
            is NetworkResult.Success -> {
                val data = postNFCIdResponseSharedFlowState.value!!.data
                if (postNFCIdResponseSharedFlowState.value!!.data != null) {
                    nfcViewModel.setNFCData(data as NFC)
                    navController.navigate(route = Screen.Main.route)
                }
            }

            is NetworkResult.Loading -> {

            }

            is NetworkResult.Error -> {

            }
            else -> {

            }
        }
    }

    LaunchedEffect(key1 = nfcScreenState.value) {
        if (nfcScreenState.value == "1") {
            nfcResponseViewModel.postNFCId(nfcScreenState.value.toInt())
        } else if (nfcScreenState.value == "SECOND") {
            navController.navigate(route = Screen.Test.route)
        }
    }

    NFCStartContent(nfcData = nfcScreenState.value)
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
                Text(
                    text = stringResource(id = R.string.nfc_tag_plz),
                    modifier = Modifier.padding(),
                    fontFamily = nanumSquareNeo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
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
