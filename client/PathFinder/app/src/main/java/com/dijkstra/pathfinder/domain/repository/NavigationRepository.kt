package com.dijkstra.pathfinder.domain.repository

import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.domain.api.NavigationApi
import com.dijkstra.pathfinder.util.SubNetworkResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

class NavigationRepository(private val navigationApi: NavigationApi) {

    var flow: Flow<SubNetworkResult<Unit>> = emptyFlow()

    suspend fun navigationTest(): Flow<SubNetworkResult<Unit>> {
        return flow {
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
        }
    }


    suspend fun navigate(start: Point, goal: Point): Flow<SubNetworkResult<List<Point>>> {

        val gson = Gson()
        val requestBody = JsonObject().apply {
            addProperty("start", gson.toJson(start))
            addProperty("goal", gson.toJson(goal))
        }
        return flow {
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
        }
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