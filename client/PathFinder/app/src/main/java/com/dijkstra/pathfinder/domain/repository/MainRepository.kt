package com.dijkstra.pathfinder.domain.repository

import com.dijkstra.pathfinder.data.dto.CurrentLocationResponse
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.SearchResponse
import com.dijkstra.pathfinder.di.AppModule
import com.dijkstra.pathfinder.domain.api.MainApi
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
    suspend fun postFacilityDynamic(searchData: String): Flow<Response<SearchResponse>> =
        flow {
            val json = JsonObject().apply {
                addProperty("filteringSearch", searchData)
            }
            emit(mainApi.postFacilityDynamic(json))
        }.flowOn(Dispatchers.IO) // End of postFacilityDynamic2

    suspend fun postCurrentLocation(point: Point): Flow<Response<CurrentLocationResponse>> =
        flow {
            val json = JsonObject().apply {
                addProperty("x", point.x)
                addProperty("y", point.y)
                addProperty("z", point.z)
            }
            emit(mainApi.postCurrnetLocation(json))
        }.flowOn(Dispatchers.IO) // End of getCurrentLocation

} // End of MainRepository class
