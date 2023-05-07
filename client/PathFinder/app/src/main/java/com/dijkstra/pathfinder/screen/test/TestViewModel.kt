package com.dijkstra.pathfinder.screen.test

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
    val testCall2SharedFlow = _testCall2SharedFlow.asSharedFlow()

    suspend fun testCall2() {
        viewModelScope.launch {
            testRepository.testCall2().onStart {
                _testCall2SharedFlow.emit(NetworkResult.Loading())
            }.catch {
                _testCall2SharedFlow.emit(
                    NetworkResult.Error(
                        it.message ?: "Something went wrong!"
                    )
                )
            }.collect {
                _testCall2SharedFlow.emit(
                    NetworkResult.Success(
                        it.code()
                    )
                )
            }
        }
    } // End of testCall2
} // End of TestViewModel class
