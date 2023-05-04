package com.dijkstra.pathfinder.screen.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val testRepository: TestRepository
) : ViewModel() {
    val testCallStateFlow: StateFlow<NetworkResult<Int>> = testRepository.testCallStateFlow

    suspend fun testCall() {
        viewModelScope.launch {
            testRepository.testCall()
        }
    } // End of testCall
} // End of TestViewModel class
