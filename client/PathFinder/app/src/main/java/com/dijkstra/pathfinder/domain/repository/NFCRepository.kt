package com.dijkstra.pathfinder.domain.repository

import com.dijkstra.pathfinder.di.AppModule
import com.dijkstra.pathfinder.domain.api.NFCApi
import com.dijkstra.pathfinder.util.NetworkResult
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
    suspend fun postNFCId(nfcId: Int): Flow<NetworkResult<Response<Void>>> = flow {
        emit(nfcApi.postNFCId(nfcId))
    }.flowOn(Dispatchers.IO)
} // End of NFCRepository class
