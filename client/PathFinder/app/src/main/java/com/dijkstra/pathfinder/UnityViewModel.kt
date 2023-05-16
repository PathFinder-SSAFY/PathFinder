package com.dijkstra.pathfinder

import android.util.Log
import androidx.lifecycle.*
import com.dijkstra.pathfinder.data.dto.NavigationResponse
import com.dijkstra.pathfinder.data.dto.Path
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.UserCameraInfo
import com.dijkstra.pathfinder.domain.repository.NavigationRepository
import com.dijkstra.pathfinder.util.Constant
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.SubNetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "UnityViewModel_ssafy"

class UnityViewModel(
    private val navigationRepository: NavigationRepository,
) : ViewModel() {

    var isVolumeMuted = false
    var isMapDisplayed = true
    var userCameraInfo: UserCameraInfo = UserCameraInfo()
    var startPosition = Point(0.0, 0.0, 0.0)
    var goal = Point(0.0, 0.0, 0.0)
    var goalName = ""

    private val _nowLocation: MutableLiveData<DoubleArray> =
        MutableLiveData(doubleArrayOf(0.0, 0.0, 0.0))
    val nowLocation: LiveData<DoubleArray> get() = _nowLocation

    private val _navigationTestNetworkResultStateFlow: MutableStateFlow<SubNetworkResult<Unit>> =
        MutableStateFlow(SubNetworkResult.Success(Unit))
    val navigationTestNetworkResultStateFlow: StateFlow<SubNetworkResult<Unit>> get() = _navigationTestNetworkResultStateFlow

    private val _navigationNetworkResultStateFlow: MutableStateFlow<SubNetworkResult<NavigationResponse>> =
        MutableStateFlow(
            SubNetworkResult.Success(
                NavigationResponse(
                    emptyList(),
                    emptyList()
                )
            )
        )
    val navigationNetworkResultStateFlow: StateFlow<SubNetworkResult<NavigationResponse>> get() = _navigationNetworkResultStateFlow

    fun navigationTest() {
        viewModelScope.launch {
            navigationRepository.navigationTest().collect { testResult ->
                _navigationTestNetworkResultStateFlow.value = testResult
            }
        }
    }

    fun navigate(start: Point, goal: Point) {
        viewModelScope.launch {
            navigationRepository.navigate(start.roundDownUnityPointTwoFive(), goal.roundDownUnityPointTwoFive()).collect() { navigateNetworkResult ->
                _navigationNetworkResultStateFlow.value = navigateNetworkResult
            }
        }
    }

    fun navigateUsingGoalName(start: Point, goalName: String) {
        viewModelScope.launch {
            navigationRepository.navigateUsingGoalName(start.roundDownUnityPointTwoFive(), goalName).collect() { navigateNetworkResult ->
                _navigationNetworkResultStateFlow.value = navigateNetworkResult
            }
        }
    }

    fun initCamera() {
        navigationRepository.initCameraAtUnity(userCameraInfo)
    }

    fun setCameraAngle() {
        navigationRepository.setCameraAngleAtUnity(userCameraInfo)
    }

    fun setCameraPosition() {
        navigationRepository.setCameraPositionAtUnity(userCameraInfo)
    }

    fun setUserCameraInfoPosition(currentPosition: Point) {
        userCameraInfo.x = currentPosition.x.toFloat()
        userCameraInfo.y = currentPosition.y.toFloat()
        userCameraInfo.z = currentPosition.z.toFloat()
    }

    fun setUserCameraInfoAngle(azimuth: Float, pitch: Float, roll: Float) {
        userCameraInfo.azimuth = azimuth
        userCameraInfo.pitch = pitch
        userCameraInfo.roll = roll
    }

    fun setNavigationPathAtUnity() {
        navigationNetworkResultStateFlow.value.data?: return
        val pointList = navigationNetworkResultStateFlow.value.data?.nodes
        if (pointList == null || pointList.isEmpty()) return
        navigationRepository.setNavigationPathAtUnity(pointList)
    }
}