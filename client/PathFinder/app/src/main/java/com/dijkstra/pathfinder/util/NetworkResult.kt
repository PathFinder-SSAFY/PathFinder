package com.dijkstra.pathfinder.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response

// =========================================================================================

sealed class NetworkResult<T>(var data: Any? = null, val message: String? = null) {
    data class Success<T> constructor(val value: T) : NetworkResult<T>(value)
    class Error<T> @JvmOverloads constructor(
        var code: Int? = null,
        var msg: String? = null,
        var exception: Throwable? = null
    ) : NetworkResult<T>(code, msg)

    class Loading<T> : NetworkResult<T>()
} // End of TestNetworkResult sealed class

fun <T> safeFlow(apiFunc: suspend () -> T): Flow<Response<T>> = flow {
    try {
        emit(Response.success(apiFunc.invoke()))
    } catch (e: HttpException) {
        emit(Response.error(e.code(), null))
    } catch (e: Exception) {
        // emit(Response.error(null, e.message))
    }
} // End of safeFlow

// =======================================================================================

sealed class SubNetworkResult<T>(var data: T? = null, val message: String? = null) {
    class Success<T>(value: T) : SubNetworkResult<T>(value)
    class Error<T>(message: String?, data: T? = null) : SubNetworkResult<T>(data, message)
    class Loading<T> : SubNetworkResult<T>()
} // End of NetworkResult sealed class

fun <T> SubSafeFlow(apiFunc: suspend () -> T): Flow<SubNetworkResult<T>> = flow {
    try {
        emit(SubNetworkResult.Success(apiFunc.invoke()))
    } catch (e: java.lang.Exception) {
        emit(SubNetworkResult.Error(e.printStackTrace().toString()))
    }
} // End of safeFlow
