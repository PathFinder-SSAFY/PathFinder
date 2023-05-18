package com.dijkstra.pathfinder.screen.nfc_start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.data.dto.NFC
import com.dijkstra.pathfinder.domain.repository.NFCRepository
import com.dijkstra.pathfinder.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NFCResponseViewModel_싸피"

@HiltViewModel
class NFCResponseViewModel @Inject constructor(
    private val nfcRepo: NFCRepository
) : ViewModel() {

    // ================================= getNavNFC =================================
    private val _postNFCIdResponseSharedFlow = MutableSharedFlow<NetworkResult<NFC>>()
    var postNFCIdResponseSharedFlow = _postNFCIdResponseSharedFlow
        private set

    fun postNFCId(nfcId: Int) {
        viewModelScope.launch {
            nfcRepo.postNFCId(nfcId).onStart {
                _postNFCIdResponseSharedFlow.emit(NetworkResult.Loading())
            }.catch {
                _postNFCIdResponseSharedFlow.emit(
                    NetworkResult.Error(
                        null, it.message, it.cause
                    )
                )
            }.collectLatest { result ->
                Log.d(TAG, "postNFCId: ${result.message()}")
                Log.d(TAG, "postNFCId result.body(): ${result.body()}")

                when {
                    result.isSuccessful -> {
                        _postNFCIdResponseSharedFlow.emit(
                            NetworkResult.Success(result.body()!!)
                        )
                    }

                    result.errorBody() != null -> {
                        _postNFCIdResponseSharedFlow.emit(
                            NetworkResult.Error(result.code(), result.message())
                        )
                    }
                }
            }
        }
    } // End of getNavNFC
} // End of NFCResponseViewModel class
