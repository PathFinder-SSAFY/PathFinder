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

    // MutableSharedFlow
    private val _testCall2SharedFlow = MutableSharedFlow<NetworkResult<Int>>()
    var testCall2SharedFlow = _testCall2SharedFlow.asSharedFlow()
        private set

    // mutableStateOf
    // Default State = NetworkResult.Loading()
    private val _testCall2ResponseFlow = mutableStateOf<NetworkResult<Int>>(NetworkResult.Loading())
    var testCall2ResponseFlow: State<NetworkResult<Int>> = _testCall2ResponseFlow
        private set

    // Default State = NetworkResult.Loading()
    private val _testCall2StateFlow = MutableStateFlow<NetworkResult<Int>>(NetworkResult.Loading())
    var testCall2StateFlow: StateFlow<NetworkResult<Int>> = _testCall2StateFlow
        private set

    /*
        testCall().onStart를 통해서 NetworkResult.Loading()으로 시작

        onStart함수는 Flow가 실행 될 때 특정데이터를 방출할 수 있도록 지원한다.
        선언 위치는 사용전이면 상관없으며 여러 번 사용하면 순서대로 전부 실행된다.
     */

    suspend fun testCall2() {
        viewModelScope.launch {
            testRepository.testCall2().onStart {
                _testCall2ResponseFlow.value = NetworkResult.Loading()
                _testCall2StateFlow.emit(NetworkResult.Loading())
                _testCall2SharedFlow.emit(NetworkResult.Loading())
            }.catch {
                it.printStackTrace()
                _testCall2ResponseFlow.value = NetworkResult.Error(
                    it.message ?: "Something went wrong!"
                )
                _testCall2StateFlow.emit(
                    NetworkResult.Error(
                        it.message ?: "Something went wrong!"
                    )
                )
            }.collect {
                _testCall2ResponseFlow.value = NetworkResult.Success(it.code())
                _testCall2StateFlow.emit(
                    NetworkResult.Success(it.code())
                )
                _testCall2SharedFlow.emit(NetworkResult.Success(it.code()))
            }
        }
    } // End of testCall2
} // End of TestViewModel class
