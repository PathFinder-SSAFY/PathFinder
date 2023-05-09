package com.dijkstra.pathfinder.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dijkstra.pathfinder.screen.test.TestViewModel
import com.dijkstra.pathfinder.util.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun TestScreen(
    navController: NavController,
    testViewModel: TestViewModel = hiltViewModel()
) {
    // val testCallState = testViewModel.testCallStateFlow.collectAsState()
    val testCall2State =
        testViewModel.testCall2SharedFlow.collectAsState(NetworkResult.Loading())

    val response = testViewModel.testCall2ResponseFlow.value

//    TestContent(data = "") {
//        CoroutineScope(Dispatchers.IO).launch {
//            testViewModel.testCall2()
//        }
//    }


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "TEST SCREEN")
            Spacer(modifier = Modifier.padding(30.dp))
            androidx.compose.material3.Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    // testViewModel.testCall()
                }
                testViewModel.testCall2()
            }) {
                Text(text = "Test Call Button")
            }

            response.let {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data == 200) {
                            Text(text = "Success")
                        }
                    }
                    is NetworkResult.Error -> {
                        Text(text = "Test Call Error")
                    }
                    is NetworkResult.Loading -> {
                        Text(text = "Test Call Loading..")
                    }
                }
            }

//            testCall2State.value.let {
//                when (it) {
//                    is NetworkResult.Success -> {
//                        if (it.data == 200) {
//                            Text(text = "Test Call Success")
//                        }
//                    }
//                    is NetworkResult.Error -> {
//                        Text(text = "Test Call Error")
//                    }
//                    is NetworkResult.Loading -> {
//                        Text(text = "Test Call Loading..")
//                    }
//                }
//            }

//            testCallState.value.let {
//                // LaunchedEffect 써야되나?? -> 안써도 되는듯
//                when (it) {
//                    is NetworkResult.Success -> {
//                        if (it.data == 200) {
//                            Text(text = "Test Call Success")
//                        }
//                    }
//                    is NetworkResult.Error -> {
//                        Text(text = "Test Call Error")
//                    }
//                    is NetworkResult.Loading -> {
//                        Text(text = "Test Call Loading..")
//                    }
//                }
//            }
        }
    }

} // End of TestScreen

@Composable
fun TestContent(data: String, onTest: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "TEST SCREEN")
            Spacer(modifier = Modifier.padding(30.dp))
            androidx.compose.material3.Button(onClick = {
                onTest()
            }) {
                Text(text = "Test Call Button")
            }


//            testCall2State.value.let {
//                when (it) {
//                    is NetworkResult.Success -> {
//                        if (it.data == 200) {
//                            Text(text = "Test Call Success")
//                        }
//                    }
//                    is NetworkResult.Error -> {
//                        Text(text = "Test Call Error")
//                    }
//                    is NetworkResult.Loading -> {
//                        Text(text = "Test Call Loading..")
//                    }
//                }
//            }

//            testCallState.value.let {
//                // LaunchedEffect 써야되나?? -> 안써도 되는듯
//                when (it) {
//                    is NetworkResult.Success -> {
//                        if (it.data == 200) {
//                            Text(text = "Test Call Success")
//                        }
//                    }
//                    is NetworkResult.Error -> {
//                        Text(text = "Test Call Error")
//                    }
//                    is NetworkResult.Loading -> {
//                        Text(text = "Test Call Loading..")
//                    }
//                }
//            }
        }
    }

} // End of TestContent

