package com.dijkstra.pathfinder.domain.api

import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.SubNetworkResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST


interface TestApi {
    @GET("hello")
    suspend fun testCall(): Response<Void>

    @GET("hello")
    suspend fun testCall2(): Response<Void>

    @POST("hello")
    suspend fun failTest(): SubNetworkResult<Response<Void>>
} // End of TestApi Interface