package com.dijkstra.pathfinder.screen.nfc_start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _getNavNFCResponseSharedFlow = MutableSharedFlow<NetworkResult<Int>>()
    var getNavNFCResponseSharedFlow = _getNavNFCResponseSharedFlow
        private set

    fun getNavNFC() {
        viewModelScope.launch {
            nfcRepo.getNavNFC().onStart {
                _getNavNFCResponseSharedFlow.emit(NetworkResult.Loading())
            }.catch {
                _getNavNFCResponseSharedFlow.emit(
                    NetworkResult.Error(
                        null, it.message, it.cause
                    )
                )
            }.collectLatest { result ->
                when {
                    result.isSuccessful -> {
                        _getNavNFCResponseSharedFlow.emit(
                            NetworkResult.Success(result.code())
                        )
                    }

                    result.errorBody() != null -> {
                        _getNavNFCResponseSharedFlow.emit(
                            NetworkResult.Error(result.code(), result.message())
                        )
                    }
                }
            }
        }
    } // End of getNavNFC
} // End of NFCResponseViewModel class
