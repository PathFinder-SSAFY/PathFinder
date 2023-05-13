package com.dijkstra.pathfinder.domain.repository

import com.dijkstra.pathfinder.data.dto.CurrentLocationResponse
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.SearchResponse
import com.dijkstra.pathfinder.data.dto.SearchValidResponse
import com.dijkstra.pathfinder.di.AppModule
import com.dijkstra.pathfinder.domain.api.MainApi
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.safeFlow
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "MainRepository_싸피"

class MainRepository @Inject constructor(
    @AppModule.OkHttpInterceptorApi private val mainApi: MainApi
) {
//    suspend fun autoCompleteSearch(value: String): Flow<NetworkResult<List<Search>>> {
//        return flow {
//            val response = mainApi.autoCompleteSearch(
//                JsonObject().apply {
//                    addProperty("filteringSearch", value)
//                }
//            )
//            when {
//                response.isSuccessful -> {
//                    emit(NetworkResult.Success(response.body()!!))
//                }
//                response.errorBody() != null -> {
//                    emit(NetworkResult.Error(response.errorBody()!!.string()))
//                }
//                else -> emit(NetworkResult.Error(response.errorBody()!!.string()))
//            }
//        }
//    }

//    suspend fun postFacilityDynamic(searchData: String): Flow<Response<SearchResponse>> = flow {
//        val json = JsonObject().apply {
//            addProperty("filteringSearch", searchData)
//        }
//        emit(mainApi.postFacilityDynamic(json))
//    }.flowOn(Dispatchers.IO) // End of postFacilityDynamic
//
//    suspend fun postFacilityDynamic2(searchData: String): Flow<NetworkResult<Response<SearchResponse>>> =
//        safeFlow {
//            val json = JsonObject().apply {
//                addProperty("filteringSearch", searchData)
//            }
//            mainApi.postFacilityDynamic(json)
//        }.flowOn(Dispatchers.IO) // End of postFacilityDynamic2

    suspend fun postFacilityDynamic(searchData: String): Flow<Response<SearchResponse>> =
        flow {
            val json = JsonObject().apply {
                addProperty("filteringSearch", searchData)
            }
            emit(mainApi.postFacilityDynamic(json))
        }.flowOn(Dispatchers.IO) // End of postFacilityDynamic2

    suspend fun postFindHelp(point: Point): Flow<Response<Point>> =
        flow {
            val json = JsonObject().apply {
                addProperty("x", point.x)
                addProperty("y", point.y)
                addProperty("z", point.z)
            }
            emit(mainApi.postFindHelp(json))
        }.flowOn(Dispatchers.IO) // End of postFindHelp

    suspend fun postCurrentLocation(point: Point): Flow<Response<CurrentLocationResponse>> =
        flow {
            val json = JsonObject().apply {
                addProperty("x", point.x)
                addProperty("y", point.y)
                addProperty("z", point.z)
            }
            emit(mainApi.postCurrnetLocation(json))
        }.flowOn(Dispatchers.IO) // End of postCurrentLocation

    suspend fun postFacilityValid(destination: String): Flow<Response<SearchValidResponse>> =
        flow {
            val json = JsonObject().apply {
                addProperty("filteringSearch", destination)
            }
            emit(mainApi.postFacilityValid(json))
        }.flowOn(Dispatchers.IO) // End of postFacilityValid


} // End of MainRepository class
