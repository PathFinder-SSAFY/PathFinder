package com.dijkstra.pathfinder

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.UserCameraInfo
import com.dijkstra.pathfinder.domain.repository.NavigationRepository
import com.dijkstra.pathfinder.util.KalmanFilter3D
import com.dijkstra.pathfinder.util.MyBluetoothHandler
import com.dijkstra.pathfinder.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "UnityViewModel_ssafy"

class UnityViewModel(
    application: android.app.Application,
    private val navigationRepository: NavigationRepository,
    private val myBluetoothHandler: MyBluetoothHandler
) : AndroidViewModel(application) {

    var isVolumeMuted = false
    var userCameraInfo: UserCameraInfo = UserCameraInfo()

    private val _nowLocation: MutableLiveData<DoubleArray> =
        MutableLiveData(doubleArrayOf(0.0, 0.0, 0.0))
    val nowLocation: LiveData<DoubleArray> get() = _nowLocation

    private val _navigationTestNetworkResultStateFlow: MutableStateFlow<NetworkResult<Unit>> =
        MutableStateFlow(NetworkResult.Success(Unit))
    val navigationTestNetworkResultStateFlow: StateFlow<NetworkResult<Unit>> get() = _navigationTestNetworkResultStateFlow

    fun navigationTest() {
        viewModelScope.launch {
            navigationRepository.navigationTest().collect { testResult ->
                _navigationTestNetworkResultStateFlow.value = testResult
            }
        }
    }

    fun navigate(start: Point, goal: Point) {
        viewModelScope.launch {
            navigationRepository.navigate(start, goal).collect() { pathList ->

            }
        }
    }

}