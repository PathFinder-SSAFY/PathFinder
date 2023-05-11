package com.dijkstra.pathfinder.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

sealed class NetworkResult<T>(var data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
} // End of NetworkResult sealed class

fun <T> safeFlow(apiFunc: suspend () -> T): Flow<NetworkResult<T>> = flow {
    try {
        emit(NetworkResult.Success(apiFunc.invoke()))
    } catch (e: java.lang.Exception) {
        emit(NetworkResult.Error(e.printStackTrace().toString()))
    }
} // End of safeFlow

// =========================================================================================

sealed class TestNetworkResult<out T>(var data: Any? = null, val message: String? = null) {
    data class Success<out T> @JvmOverloads constructor(val value: T) : TestNetworkResult<T>()
    data class Error @JvmOverloads constructor(
        var code: Int? = null,
        var msg: String? = null,
        var exception: Throwable? = null
    ) : TestNetworkResult<Nothing>()

    object Loading : TestNetworkResult<Nothing>()
} // End of TestNetworkResult sealed class

fun <T> testSafeFlow(apiFunc: suspend () -> T): Flow<Response<T>> = flow {
    try {
        emit(Response.success(apiFunc.invoke()))
    } catch (e: HttpException) {
        emit(Response.error(e.code(), null))
    } catch (e: Exception) {
        // emit(Response.error(null, e.message))
    }
} // End of safeFlow

sealed class UiState<out T>() {

} // End of UiState sealed class
