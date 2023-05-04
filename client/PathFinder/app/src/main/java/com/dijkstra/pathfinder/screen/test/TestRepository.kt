package com.dijkstra.pathfinder.screen.test

import android.util.Log
import com.dijkstra.pathfinder.di.AppModule
import com.dijkstra.pathfinder.domain.api.TestApi
import com.dijkstra.pathfinder.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val TAG = "testRepository_싸피"

class TestRepository @Inject constructor(
    @AppModule.OkHttpInterceptorApi private val testApi: TestApi
) {
    private val _testCallStateFlow = MutableStateFlow<NetworkResult<Int>>(NetworkResult.Loading())
    val testCallStateFlow: StateFlow<NetworkResult<Int>> = _testCallStateFlow.asStateFlow()

    suspend fun testCall() {
        val response = testApi.testCall()
        Log.d(TAG, "testCall response.message(): ${response.message()}")
        Log.d(TAG, "testCall response.body(): ${response.body()}")

        _testCallStateFlow.value = NetworkResult.Loading()

        when {
            response.isSuccessful -> {
                _testCallStateFlow.value = NetworkResult.Success(
                    response.code()
                )
            }
            response.errorBody() != null -> {
                _testCallStateFlow.value = NetworkResult.Error(
                    response.errorBody()!!.string()
                )
            }
        }
    } // End of testCall
} // End of testRepository class