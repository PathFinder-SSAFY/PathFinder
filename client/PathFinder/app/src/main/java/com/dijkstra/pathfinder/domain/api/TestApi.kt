package com.dijkstra.pathfinder.domain.api

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET


interface TestApi {
    @GET("hello")
    suspend fun testCall(): Response<Void>

    @GET("hello")
    suspend fun testCall2(): Response<Void>
} // End of TestApi Interface