package com.dijkstra.pathfinder.screen.nfc_start

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.data.dto.NFC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NFCViewModel_싸피"

@HiltViewModel
class NFCViewModel @Inject constructor(
) : ViewModel() {
    private val _nfcState = MutableStateFlow("")
    val nfcState = _nfcState.asStateFlow()

    private val _getNFCData = mutableStateOf<NFC?>(null)
    val getNFCData = _getNFCData

    fun setNFCData(newNFCData: NFC) {
        _getNFCData.value = newNFCData
    } // End of setNFCData


    fun setNFCState(newNFCState: String) {
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
