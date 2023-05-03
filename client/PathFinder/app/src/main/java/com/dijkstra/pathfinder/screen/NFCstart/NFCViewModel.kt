package com.dijkstra.pathfinder.screen.NFCstart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "NFCViewModel_싸피"
class NFCViewModel : ViewModel() {
    private val _nfcState = MutableStateFlow<String>("")
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
}  // End of NFCViewModel