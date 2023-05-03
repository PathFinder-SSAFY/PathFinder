package com.dijkstra.pathfinder

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dijkstra.pathfinder.data.dto.UserCameraInfoDto
import com.dijkstra.pathfinder.util.trilateration
import org.altbeacon.beacon.*

private const val TAG = "UnityViewModel_ssafy"
class UnityViewModel(application: android.app.Application) : AndroidViewModel(application) {
    var isVolumeMuted = false
    var userCameraInfoDto: UserCameraInfoDto = UserCameraInfoDto()

    private val _beaconList: MutableLiveData<List<Beacon>> = MutableLiveData(emptyList())
    val beaconList: LiveData<List<Beacon>> get() = _beaconList
    private val _nowLocation: MutableLiveData<DoubleArray> = MutableLiveData(doubleArrayOf(0.0, 0.0, 0.0))
    val nowLocation: LiveData<DoubleArray> get() = _nowLocation

    private val beaconManager = BeaconManager.getInstanceForApplication(application)
    private val region = Region(
        "estimote",
        Identifier.parse(BEACON_UUID),
        null,
        null
    )

    fun startBeaconScanning() {
        Log.d(TAG, "startBeaconScanning: ")
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
        }
    } // End of monitorNotifier

    //매초마다 해당 리전의 beacon 정보들을 collection으로 제공받아 처리한다.
    private var rangeNotifier: RangeNotifier = object : RangeNotifier {
        override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
            Log.d(TAG, "didRangeBeaconsInRegion: ")
            beacons?.run {
                _beaconList.value = beacons.toList()
                getMyLocation(beacons.toList())
            }
        }
    } // End of rangeNotifier

    fun getMyLocation(beacons: List<Beacon>) {
        Log.d(TAG, "getMyLocation: ")
        if (beacons.size < 3) {
            Log.d(TAG, "loc_0: $beacons")
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
        private const val PERMISSION_REQUEST_CODE = 8
        private const val BEACON_UUID = "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"
        private const val BEACON_MAJOR = "40011"
        private const val BEACON_MINOR = "33158"
        private const val BLUETOOTH_ADDRESS = "AC:23:3F:F6:BD:46"
        private const val BEACON_DISTANCE = 5.0
    }
}