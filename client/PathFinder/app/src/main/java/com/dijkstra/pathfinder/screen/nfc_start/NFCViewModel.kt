package com.dijkstra.pathfinder.screen.nfc_start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.domain.repository.NFCRepository
import com.dijkstra.pathfinder.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NFCViewModel_싸피"

@HiltViewModel
class NFCViewModel @Inject constructor(
) : ViewModel() {
    private val _nfcState = MutableStateFlow("")
    val nfcState = _nfcState.asStateFlow()

    fun setNFCState(newNFCState: String) {
        Log.d(TAG, "setNFCState: ")
        _nfcState.value = newNFCState
    } // End of setNFCState

    private val _sharedNFCStateFlow = MutableSharedFlow<String>()
    val sharedNFCStateFlow = _sharedNFCStateFlow.asSharedFlow()

    fun setNFCSharedFlow(newSharedNFCState: String) {
        viewModelScope.launch {
            _sharedNFCStateFlow.emit(newSharedNFCState)
        }
    } // End of setNFCSharedFlow

    // ================================= postNFCData =================================
    //    suspend fun postNFCData() : Flow<NetworkResult<Response<Void>>> {
    //
    //    }

}  // End of NFCViewModel