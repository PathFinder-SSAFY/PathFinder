package com.dijkstra.pathfinder.domain.repository

import com.dijkstra.pathfinder.data.dto.NFC
import com.dijkstra.pathfinder.di.AppModule
import com.dijkstra.pathfinder.domain.api.NFCApi
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "NFCRepository_싸피"

class NFCRepository @Inject constructor(
    @AppModule.OkHttpInterceptorApi private val nfcApi: NFCApi
) {
    // ==================================== postNFCData ====================================
    suspend fun postNFCId(nfcId: Int): Flow<Response<NFC>> = flow {
        val requestBodyJson = JsonObject().apply {
            addProperty("id", nfcId)
        }

        emit(nfcApi.postNFCId(requestBodyJson))
    }.flowOn(Dispatchers.IO)

    suspend fun getNavNFC(): Flow<Response<Void>> = flow {
        emit(nfcApi.getNavNFC())
    }.flowOn(Dispatchers.IO)
} // End of NFCRepository class
