package com.dijkstra.pathfinder

import androidx.lifecycle.*
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
    private val navigationRepository: NavigationRepository,
) : ViewModel() {

    var isVolumeMuted = false
    var userCameraInfo: UserCameraInfo = UserCameraInfo()
    var startPosition = Point(0.0, 0.0, 0.0)
    var goal = Point(0.0, 0.0, 0.0)

    private val _nowLocation: MutableLiveData<DoubleArray> =
        MutableLiveData(doubleArrayOf(0.0, 0.0, 0.0))
    val nowLocation: LiveData<DoubleArray> get() = _nowLocation

    private val _navigationTestNetworkResultStateFlow: MutableStateFlow<NetworkResult<Unit>> =
        MutableStateFlow(NetworkResult.Success(Unit))
    val navigationTestNetworkResultStateFlow: StateFlow<NetworkResult<Unit>> get() = _navigationTestNetworkResultStateFlow

    private val _navigationNetworkResultStateFlow: MutableStateFlow<NetworkResult<List<Point>>> = MutableStateFlow(NetworkResult.Success(
        emptyList()
    ))
    val navigationNetworkResultStateFlow: StateFlow<NetworkResult<List<Point>>> get() = _navigationNetworkResultStateFlow

    fun navigationTest() {
        viewModelScope.launch {
            navigationRepository.navigationTest().collect { testResult ->
                _navigationTestNetworkResultStateFlow.value = testResult
            }
        }
    }

    fun navigate(start: Point, goal: Point) {
        viewModelScope.launch {
            navigationRepository.navigate(start, goal).collect() { navigateNetworkResult ->
                _navigationNetworkResultStateFlow.value = navigateNetworkResult
            }
        }
    }

    fun initCamera() {
        navigationRepository.initCamera(userCameraInfo)
    }

    fun setCameraAngle() {
        navigationRepository.setCameraAngle(userCameraInfo)
    }

    fun setCameraPosition() {
        navigationRepository.setCameraPosition(userCameraInfo)
    }

    fun setUserCameraInfoPosition(currentPosition: Point) {
        userCameraInfo.x = currentPosition.x.toFloat()
        userCameraInfo.y = currentPosition.y.toFloat()
        userCameraInfo.z = currentPosition.z.toFloat()
    }

    fun setUserCameraInfoAngle(azimuth: Float, pitch: Float, roll: Float ) {
        userCameraInfo.azimuth = azimuth
        userCameraInfo.pitch = pitch
        userCameraInfo.roll = roll
    }
}