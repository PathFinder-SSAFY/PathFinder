package com.dijkstra.pathfinder.domain.repository

import android.util.Log
import com.dijkstra.pathfinder.data.dto.NavigationResponse
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.UserCameraInfo
import com.dijkstra.pathfinder.domain.api.NavigationApi
import com.dijkstra.pathfinder.util.SubNetworkResult
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.unity3d.player.UnityPlayer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


private const val TAG = "NavigationRepository_ssafy"

class NavigationRepository(private val navigationApi: NavigationApi) {

    suspend fun navigationTest(): Flow<SubNetworkResult<Unit>> {
        return flow {
            try {
                val response = navigationApi.navigationTest()
                emit(SubNetworkResult.Loading())
                when {
                    response.isSuccessful -> {
                        emit(SubNetworkResult.Success(response.body()!!))
                    }
                    response.errorBody() != null -> {
                        emit(SubNetworkResult.Error(response.errorBody()!!.string()))
                    }
                    else -> emit(SubNetworkResult.Error(response.errorBody()!!.string()))
                }
            } catch (e: java.lang.Exception) {
                Log.e("ssafy", "getServerCallTest: ${e.message}")
                emit(SubNetworkResult.Error(e.message))
            }
        }
    }

    suspend fun navigate(
        start: Point,
        goal: Point
    ): Flow<SubNetworkResult<NavigationResponse>> {

        val gson = Gson()
        val requestBody = JsonObject().apply {
            add("start", gson.toJsonTree(start))
            add("goal", gson.toJsonTree(goal))
            add("obstacles", JsonArray())
        }
        return flow {
            try {
                val response = navigationApi.navigate(requestBody)
                emit(SubNetworkResult.Loading())
                when {
                    response.isSuccessful -> {
                        emit(SubNetworkResult.Success(response.body()!!))
                    }
                    response.errorBody() != null -> {
                        emit(SubNetworkResult.Error(response.errorBody()!!.string()))
                    }
                    else -> emit(SubNetworkResult.Error(response.errorBody()!!.string()))
                }
            } catch (e: java.lang.Exception) {
                Log.e("ssafy", "navigate: ${e.message}")
                emit(SubNetworkResult.Error(e.message))
            }
        }
    }


    suspend fun navigateUsingGoalName(
        start: Point,
        goalName: String
    ): Flow<SubNetworkResult<NavigationResponse>> {

        val gson = Gson()
        val requestBody = JsonObject().apply {
            add("start", gson.toJsonTree(start))
            add("goal", gson.toJsonTree(goalName))
        }
        return flow {
            try {
                val response = navigationApi.navigateUsingGoalName(requestBody)
                emit(SubNetworkResult.Loading())
                when {
                    response.isSuccessful -> {
                        emit(SubNetworkResult.Success(response.body()!!))
                    }
                    response.errorBody() != null -> {
                        emit(SubNetworkResult.Error(response.errorBody()!!.string()))
                    }
                    else -> emit(SubNetworkResult.Error(response.errorBody()!!.string()))
                }
            } catch (e: java.lang.Exception) {
                Log.e("ssafy", "navigate: ${e.message}")
                emit(SubNetworkResult.Error(e.message))
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