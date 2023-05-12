package com.dijkstra.pathfinder.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dijkstra.pathfinder.screen.test.TestViewModel
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.SubNetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "TestScreen_싸피"

@Composable
fun TestScreen(
    navController: NavController,
    testViewModel: TestViewModel = hiltViewModel()
) {
    // val testCallState = testViewModel.testCallStateFlow.collectAsState()
    // val testCall2State = testViewModel.testCall2SharedFlow.collectAsState(NetworkResult.Loading())

    val response = testViewModel.testCall2ResponseFlow.value
    var responseResultData by remember {
        mutableStateOf<SubNetworkResult<Int>>(SubNetworkResult.Loading())
    }

    val testCall2StateFlowResponse = testViewModel.testCall2StateFlow.collectAsState()

    val collectAs = testViewModel.testCall2SharedFlow.collectAsState(SubNetworkResult.Loading())
    collectAs.value.let {
        // 이거 안됌
        Log.d(TAG, "collectAs: $it")
        responseResultData = it
    }

    val failTestResponseStateFlowCollect = testViewModel.failTestResponseStateFlow.collectAsState()

    val failTestResposneSharedFlowCollect =
        testViewModel.failTestResponseSharedFlow.collectAsState(null)

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
                    testViewModel.testCall2()
                    testViewModel.failTest()
                }
            }) {
                Text(text = "Test Call Button")
            }

            collectAs.value.let {
                when (it) {
                    is SubNetworkResult.Success -> {
                        if (it.data == 200) {
                            Text(text = "collectAs : Success")
                        }
                    }
                    is SubNetworkResult.Error -> {
                        Text(text = "Test Call Error")
                    }
                    is SubNetworkResult.Loading -> {
                        Text(text = "Test Call Loading..")
                    }
                }
            }

            Spacer(Modifier.padding(40.dp))

            Text(text = "아래는 SharedFlow결과값을 관찰해서 mutableState responseResultData로 값을 전달.")

            responseResultData.let {
                when (it) {
                    is SubNetworkResult.Success -> {
                        if (it.data == 200) {
                            Text(text = "responseResultData : Success")
                        }
                    }
                    is SubNetworkResult.Error -> {
                        Text(text = "Test Call Error")
                    }
                    is SubNetworkResult.Loading -> {
                        Text(text = "Test Call Loading..")
                    }
                }
            }

            Spacer(Modifier.padding(40.dp))
            Text(text = "아래는 StateFlow.let으로 짜여진 코드")
            testCall2StateFlowResponse.let {
                when (it.value) {
                    is SubNetworkResult.Success -> {
                        if (it.value.data == 200) {
                            Text(text = "testCall2StateFlowResponse : Success")
                        }
                    }
                    is SubNetworkResult.Error -> {
                        Text(text = "Test Call Error")
                    }
                    is SubNetworkResult.Loading -> {
                        Text(text = "Test Call Loading..")
                    }
                }
            }

            // ================================ FailTest ================================
            Spacer(Modifier.padding(40.dp))
            Text(text = "아래는 실패 테스트 결과 값임")
            failTestResponseStateFlowCollect.let {
                when (it.value) {
                    is SubNetworkResult.Success -> {
                        if (it.value?.data == 200) {
                            Text(text = "collectAs : Success")
                        }
                    }
                    is SubNetworkResult.Error -> {
                        Text(text = "Test Call Error")
                    }
                    is SubNetworkResult.Loading -> {
                        Text(text = "Test Call Loading..")
                    }
                    else -> {
                        Text(text = "")
                    }
                }
            }

            Spacer(Modifier.padding(40.dp))
            Text(text = "아래는 실패 테스트 결과 값 : FailTestResponseSharedFlow")
            failTestResposneSharedFlowCollect.let {
                when (it.value!!) {
                    is SubNetworkResult.Success -> {
                        if (it.value?.data == 200) {
                            Text(text = "FailTestResponse SharedFlow Result : Success")
                        }
                    }

                    is SubNetworkResult.Error -> {
                        Text(text = "FailTestResponse SharedFlow Result : Error")
                    }

                    is SubNetworkResult.Loading -> {
                        Text(text = "FailTestResponse SharedFlow Result : Loading")
                    }
                }
            }
        }
    }
} // End of TestScreen


