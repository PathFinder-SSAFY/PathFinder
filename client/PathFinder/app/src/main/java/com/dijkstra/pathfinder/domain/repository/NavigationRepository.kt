package com.dijkstra.pathfinder.domain.repository

import android.util.Log
import com.dijkstra.pathfinder.data.dto.*
import com.dijkstra.pathfinder.domain.api.NavigationApi
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.tempPointList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val TAG = "NavigationRepository_ssafy"
class NavigationRepository(private val navigationApi: NavigationApi) {

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

    suspend fun navigate(start: Point, goal: Point): Flow<NetworkResult<NavigationResponse>> {

        val gson = GsonBuilder().create()
        val requestBody = JsonObject().apply {
            add("start", gson.toJsonTree(start))
            add("goal", gson.toJsonTree(goal))
            add("obstacles", JsonArray())
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

    fun initCameraAtUnity(userCameraInfo: UserCameraInfo) {
        UnityPlayer.UnitySendMessage(
            "SystemController",
            "InitARCameraTransform",
            userCameraInfo.toString()
        )
    }

    fun setCameraAngleAtUnity(userCameraInfo: UserCameraInfo) {
        UnityPlayer.UnitySendMessage(
            "SystemController",
            "SetARCameraAngle",
            userCameraInfo.toString()
        )
    } // End of initCameraPosition

    fun setCameraPositionAtUnity(userCameraInfo: UserCameraInfo) {
        UnityPlayer.UnitySendMessage(
            "SystemController",
            "SetARCameraPosition",
            userCameraInfo.toString()
        )
    }

    fun setNavigationPathAtUnity(pathList: List<Point>) {
        if (pathList.isEmpty()) return;
        Log.d(TAG, "setNavigationPathAtUnity: $pathList")
        UnityPlayer.UnitySendMessage(
            "Indicator",
            "SetNavigationPath",
            GsonBuilder().create().toJson(
                pathList.reversed()
            )
        )
//        UnityPlayer.UnitySendMessage(
//            "Indicator",
//            "SetNavigationPath",
//            tempPointList
//        )
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