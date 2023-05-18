package com.dijkstra.pathfinder.domain.api

import com.dijkstra.pathfinder.data.dto.NFC
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NFCApi {

    @GET("hello")
    suspend fun getNavNFC(): Response<Void>

    // NFC태깅을 해서 현재위치를 받아오는 api
    @POST("building/nfc")
    suspend fun postNFCId(
        @Body id: JsonObject
    ): Response<NFC>
} // End of NFCApi Interface
