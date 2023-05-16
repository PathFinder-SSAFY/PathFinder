package com.dijkstra.pathfinder.domain.api

import com.dijkstra.pathfinder.data.dto.*
import com.dijkstra.pathfinder.util.NetworkResult
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface MainApi {
    //    http://k8d206.p.ssafy.io/api/

    @POST("pathfinding/findHelp")
    suspend fun postFindHelp(
        @Body requestBody: JsonObject
    ): Response<Point>

    @POST("floors/curloc")
    suspend fun postCurrnetLocation(
        @Body requestBody: JsonObject
    ): Response<CurrentLocationResponse>

    @PATCH("floors/update/customer/location/{id}")
    suspend fun patchCurrentLocation(
        @retrofit2.http.Path("id") id: String,
        @Body requestBody: JsonObject
    ): Response<CurrentLocationResponse>

    @POST("facility/improvements")
    suspend fun postFacilityValid(
        @Body filteringSearch: JsonObject
    ): Response<SearchValidResponse>

    @POST("facility/dynamic")
    suspend fun postFacilityDynamic(
        @Body filteringSearch: JsonObject
    ): Response<SearchResponse>

    @POST("facility/search")
    suspend fun postFacilitySearch(
        @Body filteringSearch: JsonObject
    ): NetworkResult<Response<Void>>

} // End of MainApi Interface
