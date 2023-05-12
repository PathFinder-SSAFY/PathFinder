package com.dijkstra.pathfinder.screen.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.R
import com.dijkstra.pathfinder.domain.repository.MainRepository
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
    private val mainRepo: MainRepository
) : ViewModel() {
    var beaconList: MutableState<List<Beacon>> = mutableStateOf(emptyList())
    var kalmanLocation = mutableStateOf(listOf(0.0, 0.0, 0.0))
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
                getMyLocation(beacons.toList())
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

    fun getMyLocation(beacons: List<Beacon>) {
        if (beacons.size < 3) {
            Log.d(TAG, "getMyLocation: nope")
            return
        } else {
            var sortedList = beacons.sortedBy { it.distance }
            var centroid = trilateration(sortedList).toList()

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
        Log.d(TAG, "startRangingBeacons: ")
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
                        _postFacilityDynamicResponseSharedFlow.emit(
                            NetworkResult.Success(result.body()!!.data)
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
} // End of MainViewModel class
