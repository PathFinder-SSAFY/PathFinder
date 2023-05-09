package com.dijkstra.pathfinder.domain.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface NavigationApi {
    @GET("findPath")
    suspend fun navigationTest(): Response<Unit>


}