package com.dijkstra.pathfinder.screen.test

import com.dijkstra.pathfinder.di.AppModule
import com.dijkstra.pathfinder.domain.api.TestApi
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.SubNetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "testRepository_싸피"

class TestRepository @Inject constructor(
    @AppModule.OkHttpInterceptorApi private val testApi: TestApi
) {
    private val _testCallStateFlow = MutableStateFlow<SubNetworkResult<Int>>(SubNetworkResult.Loading())
    val testCallStateFlow: StateFlow<SubNetworkResult<Int>> = _testCallStateFlow.asStateFlow()

    suspend fun testCall() {
        val response = testApi.testCall()

        _testCallStateFlow.value = SubNetworkResult.Loading()
        when {
            response.isSuccessful -> {
                _testCallStateFlow.value = SubNetworkResult.Success(
                    response.code()
                )
            }
            response.errorBody() != null -> {
                _testCallStateFlow.value = SubNetworkResult.Error(
                    response.errorBody()!!.string()
                )
            }
        }
    } // End of testCall

    suspend fun testCall2(): Flow<Response<Void>> = flow {
        emit(testApi.testCall2())
    }.flowOn(Dispatchers.IO)  // End of testCall2

    suspend fun failTest(): Flow<SubNetworkResult<Response<Void>>> = flow {
        emit(testApi.failTest())
    }.flowOn(Dispatchers.IO)

} // End of testRepository class
