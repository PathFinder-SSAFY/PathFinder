package com.dijkstra.pathfinder.domain.api

import com.dijkstra.pathfinder.data.dto.Point
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NavigationApi {
    @GET("findPath")
    suspend fun navigationTest(): Response<Unit>

    @POST("findPath/protoType")
    suspend fun navigate(@Body requestBody: JsonObject): Response<List<Point>>

}