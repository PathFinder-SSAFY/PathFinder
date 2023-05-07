package com.dijkstra.pathfinder.screen.test

import com.dijkstra.pathfinder.di.AppModule
import com.dijkstra.pathfinder.domain.api.TestApi
import com.dijkstra.pathfinder.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "testRepository_싸피"

class TestRepository @Inject constructor(
    @AppModule.OkHttpInterceptorApi private val testApi: TestApi
) {
    private val _testCallStateFlow = MutableStateFlow<NetworkResult<Int>>(NetworkResult.Loading())
    val testCallStateFlow: StateFlow<NetworkResult<Int>> = _testCallStateFlow.asStateFlow()

    suspend fun testCall() {
        val response = testApi.testCall()

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

    suspend fun testCall2(): Flow<Response<Void>> = flow {
        emit(testApi.testCall2())
    }.flowOn(Dispatchers.IO)  // End of testCall2
} // End of testRepository class
