package com.dijkstra.pathfinder.domain.repository

import com.dijkstra.pathfinder.data.dto.Search
import com.dijkstra.pathfinder.domain.api.MainApi
import com.dijkstra.pathfinder.util.NetworkResult
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val mainApi: MainApi) {

    suspend fun autoCompleteSearch(value: String): Flow<NetworkResult<List<Search>>> {
        return flow {
            val response = mainApi.autoCompleteSearch(
                JsonObject().apply {
                    addProperty("filteringSearch", value)
                }
            )
            when {
                response.isSuccessful -> {
                    emit(NetworkResult.Success(response.body()!!))
                }
                response.errorBody() != null -> {
                    emit(NetworkResult.Error(response.errorBody()!!.string()))
                }
                else -> emit(NetworkResult.Error(response.errorBody()!!.string()))
            }
        }
    }


}