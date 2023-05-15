package com.dijkstra.pathfinder.screen.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.R
import com.dijkstra.pathfinder.data.dto.BeaconPosition
import com.dijkstra.pathfinder.data.dto.CurrentLocationResponse
import com.dijkstra.pathfinder.data.dto.Point
import com.dijkstra.pathfinder.data.dto.SearchValidResponse
import com.dijkstra.pathfinder.domain.repository.MainRepository
import com.dijkstra.pathfinder.screen.nfc_start.NFCViewModel
import com.dijkstra.pathfinder.util.Constant
import com.dijkstra.pathfinder.util.KalmanFilter3D
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.trilateration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.altbeacon.beacon.*
import javax.inject.Inject

private const val TAG = "MainViewModel_SSAFY"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val mainRepo: MainRepository,
) : ViewModel() {
    var beaconList: MutableState<List<Beacon>> = mutableStateOf(emptyList()) // bluetooth로 찾은 beacon
    var kalmanLocation = mutableStateOf(listOf(-9999.0, -9999.0, -9999.0))
    var tempLocationPoint = Point(-9999.0, -9999.0, -9999.0)
    var currentLocationPoint = Point(-9999.0, -9999.0, -9999.0)
    var tempLocationName = ""
    var currentLocationName = mutableStateOf("")
    var destinationLocationPoint: Point? = null
    var destinationLocationName = mutableStateOf("")

    var allBeaconList: List<BeaconPosition> = emptyList()

    @JvmName("set")
    fun setAllBeaconList(newBeaconList: List<BeaconPosition>) {
        allBeaconList = newBeaconList
    }

    private val beaconManager = BeaconManager.getInstanceForApplication(application)

    private val region = Region(
        application.getString(R.string.beacon_unique_id),
        Identifier.parse(application.getString(R.string.beacon_uuid)),
        null,
        null
    )

    //매초마다 해당 리전의 beacon 정보들을 collection으로 제공받아 처리한다.
    private var rangeNotifier: RangeNotifier = object : RangeNotifier {
        override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
            beacons?.run {
                beaconList.value = beacons.toList()
                getMyLocation(beacons.toList(), allBeaconList)
            }
        }
    } // End of rangeNotifier

    private val kalmanFilter = KalmanFilter3D(
        initialState = listOf(0.0, 0.0, 0.0),
        initialCovariance = listOf(
            listOf(1.0, 0.0, 0.0),
            listOf(0.0, 1.0, 0.0),
            listOf(0.0, 0.0, 1.0)
        )
    )

    fun getMyLocation(beacons: List<Beacon>, allBeaconList: List<BeaconPosition>) {
        if (beacons.size < 3) {
            return
        } else {
            var sortedList = beacons.sortedBy { it.distance }
            // 전체 비콘 리스트가 필요
            var centroid = trilateration(sortedList, allBeaconList).toList()

            if (centroid == listOf(-9999.9, -9999.9, -9999.9)) {
                return
            }

            // Apply the Kalman filter to the centroid
            val measurementNoise =
                listOf(1.0, 1.0, 1.0) // Adjust this value based on your measurement noise
            val filteredCentroid = kalmanFilter.update(centroid, measurementNoise)

            Log.d(TAG, "getMyLocation: $filteredCentroid")

            kalmanLocation.value = filteredCentroid
            return
        }
    } // End of getMyLocation

    fun startRangingBeacons() {
        beaconManager.startRangingBeacons(region)
    }

    init {
        beaconManager.apply {
            beaconParsers.add(
                BeaconParser().setBeaconLayout(application.getString(R.string.beacon_layout))
            )
            addRangeNotifier(rangeNotifier)
        }
    }


    override fun onCleared() {
        beaconManager.stopRangingBeacons(region)
        beaconManager.removeAllRangeNotifiers()
        super.onCleared()
    }

    // ========================================== postFacilityDynamicSearch ==========================================

//    private val _postFacilityDynamicResponseStateFlow =
//        MutableStateFlow<NetworkResult<SearchResponse>?>(null)
//    var postFacilityDynamicResponseStateFlow = _postFacilityDynamicResponseStateFlow
//        private set

    private val _postFacilityDynamicResponseSharedFlow =
        MutableSharedFlow<NetworkResult<MutableList<String>>>(0)
    var postFacilityDynamicResponseSharedFlow = _postFacilityDynamicResponseSharedFlow
        private set

    fun postFacilityDynamic(searchData: String) {
        viewModelScope.launch {
            mainRepo.postFacilityDynamic(searchData).onStart {
                _postFacilityDynamicResponseSharedFlow.emit(NetworkResult.Loading())
            }.catch { result ->
                _postFacilityDynamicResponseSharedFlow.emit(
                    NetworkResult.Error(
                        null,
                        result.message,
                        result.cause
                    )
                )
            }.collectLatest { result ->
                when {
                    result.isSuccessful && result.body() != null -> {
                        Log.d(TAG, "postFacilityDynamic: ${result.body()}")
                        _postFacilityDynamicResponseSharedFlow.emit(
                            NetworkResult.Success(result.body()!!.responseData)
                        )
                    }

                    result.errorBody() != null -> {
                        _postFacilityDynamicResponseSharedFlow.emit(
                            NetworkResult.Error(
                                result.code(),
                                result.message()
                            )
                        )
                    }
                }
            }
        }
    } // End of postFacilityDynamic

    // ========================================== postCurrentLocation ==========================================
    private val _postCurrentLocationResponseSharedFlow =
        MutableSharedFlow<NetworkResult<CurrentLocationResponse>>(0)
    var postCurrentLocationResponseSharedFlow = _postCurrentLocationResponseSharedFlow
        private set

    fun postCurrentLocation(point: Point) {
        viewModelScope.launch {
            mainRepo.postCurrentLocation(point)
                .onStart {
                    tempLocationPoint = Point(-9999.0, -9999.0, -9999.0)
                    _postCurrentLocationResponseSharedFlow.emit(NetworkResult.Loading())
                }
                .catch { response ->
                    _postCurrentLocationResponseSharedFlow.emit(
                        NetworkResult.Error(
                            null,
                            response.message,
                            response.cause
                        )
                    )
                }
                .collectLatest { response ->
                    when {
                        response.isSuccessful && response.body() != null -> {
                            tempLocationPoint = point
                            tempLocationName = response.body()!!.responseData
                            _postCurrentLocationResponseSharedFlow.emit(
                                NetworkResult.Success(response.body()!!)
                            )
                        }
                        response.errorBody() != null -> {
                            _postCurrentLocationResponseSharedFlow.emit(
                                NetworkResult.Error(
                                    response.code(),
                                    response.message()
                                )
                            )
                        }
                    }
                }
        }
    } // End of postCurrentLocation
    
    // ========================================== postFindHelp ==========================================

    private val _postFindHelpResponseSharedFlow = MutableSharedFlow<NetworkResult<Point>>(0)
    var postFindHelpResponseSharedFlow = _postFindHelpResponseSharedFlow
        private set

    fun postFindHelp(help: Int, point: Point) {
        viewModelScope.launch {
            mainRepo.postFindHelp(help, point)
                .onStart {
                    _postFindHelpResponseSharedFlow.emit(NetworkResult.Loading())
                }
                .catch { response ->
                    _postFindHelpResponseSharedFlow.emit(
                        NetworkResult.Error(
                            null,
                            response.message,
                            response.cause
                        )
                    )
                }
                .collectLatest { response ->
                    when {
                        response.isSuccessful && response.body() != null -> {
                            if (help == Constant.AED) {
                                destinationLocationName.value = application.getString(R.string.aed)
                            } else if (help == Constant.FIRE) {
                                destinationLocationName.value = application.getString(R.string.fire_extinguisher)
                            }
                            destinationLocationPoint = response.body()
                            _postFindHelpResponseSharedFlow.emit(
                                NetworkResult.Success(response.body()!!)
                            )
                        }
                        response.errorBody() != null -> {
                            _postFindHelpResponseSharedFlow.emit(
                                NetworkResult.Error(
                                    response.code(),
                                    response.message(),
                                )
                            )
                        }
                    }
                } // End of collectLatest
        } // End of postFindHelp
    } // End of postFindHelp

    // ========================================== postFacilityValid ==========================================

    private val _postSearchValidResponseSharedFlow = MutableSharedFlow<NetworkResult<SearchValidResponse>>(0)
    var postSearchValidResponseSharedFlow = _postSearchValidResponseSharedFlow
        private set

    fun postFacilityValid(destination: String) {
        viewModelScope.launch {
            mainRepo.postFacilityValid(destination)
                .onStart {
                    _postSearchValidResponseSharedFlow.emit(NetworkResult.Loading())
                }
                .catch { response ->
                    _postSearchValidResponseSharedFlow.emit(
                        NetworkResult.Error(
                            null,
                            response.message,
                            response.cause
                        )
                    )
                }
                .collectLatest { response ->
                    when {
                        response.isSuccessful && response.body() != null -> {
                            _postSearchValidResponseSharedFlow.emit(
                                NetworkResult.Success(response.body()!!)
                            )
                        }
                        response.errorBody() != null -> {
                            _postSearchValidResponseSharedFlow.emit(
                                NetworkResult.Error(
                                    response.code(),
                                    response.message(),
                                )
                            )
                        }
                    }
                } // End of collectLatest
        } // End of viewModelScope
    } // End of postFacilityValid

} // End of MainViewModel class
