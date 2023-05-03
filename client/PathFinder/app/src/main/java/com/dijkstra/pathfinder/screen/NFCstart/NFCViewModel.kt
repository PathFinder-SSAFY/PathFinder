package com.dijkstra.pathfinder.screen.NFCstart

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "NFCViewModel_싸피"
class NFCViewModel : ViewModel() {
    private val _nfcState = MutableStateFlow<String>("")
    val nfcState: StateFlow<String> = _nfcState

    fun setNFCState(newNFCState: String) {
        Log.d(TAG, "setNFCState: ")
        _nfcState.value = newNFCState
    } // End of setNFCState
}  // End of NFCViewModel