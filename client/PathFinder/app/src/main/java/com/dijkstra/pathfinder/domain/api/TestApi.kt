package com.dijkstra.pathfinder.domain.api

import retrofit2.Response
import retrofit2.http.GET


interface TestApi {
    @GET("/hello")
    suspend fun testCall(): Response<Void>
} // End of TestApi Interface