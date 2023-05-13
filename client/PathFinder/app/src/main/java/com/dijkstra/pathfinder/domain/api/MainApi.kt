package com.dijkstra.pathfinder.domain.api

import com.dijkstra.pathfinder.data.dto.CurrentLocationResponse
import com.dijkstra.pathfinder.data.dto.Search
import com.dijkstra.pathfinder.data.dto.SearchResponse
import com.dijkstra.pathfinder.util.NetworkResult
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface MainApi {
    //    http://k8d206.p.ssafy.io/api/
    @POST("facility/curloc")
    suspend fun postCurrnetLocation(
        @Body requestBody: JsonObject
    ): Response<CurrentLocationResponse>

    @POST("facility/dynamic")
    suspend fun postFacilityDynamic(
        @Body filteringSearch: JsonObject
    ): Response<SearchResponse>

    @POST("facility/search")
    suspend fun postFacilitySearch(
        @Body filteringSearch: JsonObject
    ): NetworkResult<Response<Void>>
} // End of MainApi Interface
