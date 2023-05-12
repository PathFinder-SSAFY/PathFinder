package com.dijkstra.pathfinder.domain.api

import com.dijkstra.pathfinder.util.NetworkResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NFCApi {

    // NFC태깅을 해서 현재위치를 받아오는 api
    @POST("")
    suspend fun postNFCId(
        @Body nfcId : Int
    ) : NetworkResult<Response<Void>>
} // End of NFCApi Interface
