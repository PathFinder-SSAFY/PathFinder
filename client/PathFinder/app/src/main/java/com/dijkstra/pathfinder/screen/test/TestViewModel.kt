package com.dijkstra.pathfinder.screen.test

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    // ================================ LiveData 코드 Flow코드로 수정

    private val _testCall2SharedFlow = MutableSharedFlow<NetworkResult<Int>>()
    var testCall2SharedFlow = _testCall2SharedFlow.asSharedFlow()
        private set

    // Default State = NetworkResult.Loading()
    private val _testCall2ResponseFlow = mutableStateOf<NetworkResult<Int>>(NetworkResult.Loading())
    var testCall2ResponseFlow: State<NetworkResult<Int>> = _testCall2ResponseFlow
        private set

    fun testCall2() {
        viewModelScope.launch {
            testRepository.testCall2().onStart {
                _testCall2SharedFlow.emit(NetworkResult.Loading())
            }.catch {
                it.printStackTrace()
                _testCall2ResponseFlow.value = NetworkResult.Error(
                    it.message ?: "Something went wrong!"
                )
            }.collect {
                _testCall2ResponseFlow.value = NetworkResult.Success(it.code())
            }
        }
    } // End of testCall2
} // End of TestViewModel class
