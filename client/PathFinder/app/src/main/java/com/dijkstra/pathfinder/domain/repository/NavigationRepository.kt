package com.dijkstra.pathfinder.domain.repository

import android.util.Log
import android.widget.Toast
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.Search
import com.dijkstra.pathfinder.data.dto.UserCameraInfo
import com.dijkstra.pathfinder.domain.api.NavigationApi
import com.dijkstra.pathfinder.util.NetworkResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NavigationRepository (private val navigationApi: NavigationApi) {

    suspend fun navigationTest(): Flow<NetworkResult<Unit>> {
        return flow {

            try {
                val response = navigationApi.navigationTest()
                emit(NetworkResult.Loading())
                when {
                    response.isSuccessful -> {
                        emit(NetworkResult.Success(response.body()!!))
                    }
                    response.errorBody() != null -> {
                        emit(NetworkResult.Error(response.errorBody()!!.string()))
                    }
                    else -> emit(NetworkResult.Error(response.errorBody()!!.string()))
                }
            } catch (e: java.lang.Exception) {
                Log.e("ssafy", "getServerCallTest: ${e.message}")
                emit(NetworkResult.Error(e.message))
            }
        }
    }

    suspend fun navigate(start: Point, goal: Point): Flow<NetworkResult<List<Point>>> {

        val gson = Gson()
        val requestBody = JsonObject().apply {
            addProperty("start", gson.toJson(start))
            addProperty("goal", gson.toJson(goal))
        }
        return flow {
            try {
                val response = navigationApi.navigate(requestBody)
                emit(NetworkResult.Loading())
                when {
                    response.isSuccessful -> {
                        emit(NetworkResult.Success(response.body()!!))
                    }
                    response.errorBody() != null -> {
                        emit(NetworkResult.Error(response.errorBody()!!.string()))
                    }
                    else -> emit(NetworkResult.Error(response.errorBody()!!.string()))
                }
            } catch (e: java.lang.Exception) {
                Log.e("ssafy", "navigate: ${e.message}")
                emit(NetworkResult.Error(e.message))
            }
        }
    }

    fun initCamera(userCameraInfo: UserCameraInfo) {
        UnityPlayer.UnitySendMessage(
            "SystemController",
            "InitARCameraTransform",
            userCameraInfo.toString()
        )
    }

    fun setCameraAngle(userCameraInfo: UserCameraInfo) {
        UnityPlayer.UnitySendMessage(
            "SystemController",
            "SetARCameraAngle",
            userCameraInfo.toString()
        )
    } // End of initCameraPosition

    fun setCameraPosition(userCameraInfo: UserCameraInfo) {
        UnityPlayer.UnitySendMessage(
            "SystemController",
            "SetARCameraPosition",
            userCameraInfo.toString()
        )
    }



    companion object {
        private var INSTANCE: NavigationRepository? = null
        fun getInstance(navigationApi: NavigationApi): NavigationRepository {
            if (INSTANCE == null) {
                INSTANCE = NavigationRepository(navigationApi)
            }
            return INSTANCE!!
        }
    }

}