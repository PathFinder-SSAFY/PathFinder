package com.dijkstra.pathfinder.domain.api

import com.dijkstra.pathfinder.data.dto.Search
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface MainApi {
    //    http://k8d206.p.ssafy.io/api/
    @POST
    suspend fun autoCompleteSearch(
        @Body requestBody: JsonObject
    ): Response<List<Search>>

}