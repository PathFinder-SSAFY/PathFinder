package com.dijkstra.pathfinder

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dijkstra.pathfinder.data.dto.UserCameraInfoDto
import com.dijkstra.pathfinder.util.trilateration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.altbeacon.beacon.*

private const val TAG = "UnityViewModel_ssafy"
class UnityViewModel(application: android.app.Application) : AndroidViewModel(application) {
    var isVolumeMuted = false
    var userCameraInfoDto: UserCameraInfoDto = UserCameraInfoDto()

    private val _beaconListStateFlow: MutableStateFlow<List<Beacon>> = MutableStateFlow(emptyList())
    val beaconListStateFlow: StateFlow<List<Beacon>> get() = _beaconListStateFlow

    private val _beaconList: MutableLiveData<MutableList<Beacon>> = MutableLiveData<MutableList<Beacon>>()
    val beaconList: LiveData<MutableList<Beacon>> get() = _beaconList
    private val _nowLocation: MutableLiveData<DoubleArray> = MutableLiveData(doubleArrayOf(0.0, 0.0, 0.0))
    val nowLocation: LiveData<DoubleArray> get() = _nowLocation

    private val beaconManager = BeaconManager.getInstanceForApplication(application)
    private val region = Region(
        "estimote",
        Identifier.parse(BEACON_UUID),
        null,
        null
    )

    fun setBeaconList(newBeaconList: List<Beacon>) {
        Log.d(TAG, "setBeaconList: $newBeaconList")
        val tempList = mutableListOf<Beacon>()
        tempList.addAll(newBeaconList)
        _beaconList.value?.clear()
        _beaconList.value = tempList
        Log.d(TAG, "setBeaconList2: ${_beaconList.value}")
    }

    fun setNowLoacation(newLoacation: DoubleArray) {
        _nowLocation.value = newLoacation
    }

    fun startBeaconScanning() {
        Log.d(TAG, "startBeaconScanning: ")
        beaconManager.setScannerInSameProcess(true)
        beaconManager.apply {
            beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
            addMonitorNotifier(monitorNotifier)
            addRangeNotifier(rangeNotifier)
            startMonitoring(region)
            startRangingBeacons(region)
        }
    } // End of scanToggle

    fun stopBeaconScanning() {
        Log.d(TAG, "stopBeaconScanning: ")
        beaconManager.stopMonitoring(region)
        beaconManager.stopRangingBeacons(region)
    }


    private var monitorNotifier: MonitorNotifier = object : MonitorNotifier {
        override fun didEnterRegion(region: Region) { //발견 함.
            Log.d(TAG, "I just saw an beacon for the first time!")
        }

        override fun didExitRegion(region: Region) { //발견 못함.
            Log.d(TAG, "I no longer see an beacon")
        }

        override fun didDetermineStateForRegion(state: Int, region: Region) { //상태변경
            Log.d(TAG, "I have just switched from seeing/not seeing beacons: $state")
            Log.d(
                TAG,
                "didDetermineStateForRegion: isSameProcess? ${beaconManager.isScannerInDifferentProcess}"
            )
        }
    } // End of monitorNotifier

    //매초마다 해당 리전의 beacon 정보들을 collection으로 제공받아 처리한다.
    private var rangeNotifier: RangeNotifier = object : RangeNotifier {
        override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
            beacons?.run {
                val temp = mutableListOf<Beacon>()
                temp.addAll(beacons)
                _beaconList.value?.clear()
                _beaconList.value = temp
                getMyLocation(beacons.toList())
            }
        }
    } // End of rangeNotifier

    fun getMyLocation(beacons: List<Beacon>) {
        if (beacons.size < 3) {
            Log.d(TAG, "loc_0: $beacons")
            _nowLocation.value = doubleArrayOf(0.0, 0.3, 0.5)
            return
        } else {
            var sortedList = beacons.sortedBy { it.distance }
            var centroid = trilateration(sortedList)
            for (i in centroid.indices) {
                Log.d(TAG, "loc_cen${i}: ${centroid[i]}")
            }
            _nowLocation.value = centroid
            return
        }
    }

    companion object {
        private const val BEACON_UUID = "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"
    }
}