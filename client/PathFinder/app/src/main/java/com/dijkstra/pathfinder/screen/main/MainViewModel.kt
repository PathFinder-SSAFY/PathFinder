package com.dijkstra.pathfinder.screen.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dijkstra.pathfinder.data.dto.SearchResponse
import com.dijkstra.pathfinder.domain.repository.MainRepository
import com.dijkstra.pathfinder.util.KalmanFilter3D
import com.dijkstra.pathfinder.util.NetworkResult
import com.dijkstra.pathfinder.util.trilateration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.altbeacon.beacon.*
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "MainViewModel_싸피"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val mainRepo: MainRepository
) : ViewModel() {
    var beaconList: MutableState<List<Beacon>> = mutableStateOf(emptyList())
    var kalmanLocation = mutableStateOf(listOf(0.0, 0.0, 0.0))
    private val beaconManager = BeaconManager.getInstanceForApplication(application)

    private val region = Region(
        "estimote",
        Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"),
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

            filteredCentroid.forEach {
                Log.d(TAG, "getMyLocation: $it")
            }

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
                BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
            )
            addRangeNotifier(rangeNotifier)
        }
        Log.d(TAG, "init: $beaconManager")
    }


    override fun onCleared() {
        beaconManager.stopRangingBeacons(region)
        beaconManager.removeAllRangeNotifiers()
        Log.d(TAG, "onCleared: gone")
        super.onCleared()
    }

    // ========================================== postFacilityDynamic ==========================================

    private val _postFacilityDynamicResponseStateFlow =
        MutableStateFlow<NetworkResult<Response<SearchResponse>>?>(null)
    var postFacilityDynamicResponseStateFlow = _postFacilityDynamicResponseStateFlow
        private set

    suspend fun postFacilityDynamic(searchData: String) {
        viewModelScope.launch {
            mainRepo.postFacilityDynamic(searchData).onStart {
                _postFacilityDynamicResponseStateFlow.emit(
                    NetworkResult.Loading()
                )
            }.catch {
                _postFacilityDynamicResponseStateFlow.emit(
                    NetworkResult.Error(
                        it.message.toString()
                    )
                )
            }
                .collectLatest {
                    _postFacilityDynamicResponseStateFlow.emit(
                        NetworkResult.Success(it)
                    )
                }
        }
    } // End of postFacilityDynamic
} // End of MainViewModel class
