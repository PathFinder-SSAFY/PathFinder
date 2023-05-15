package com.dijkstra.pathfinder.util

import android.util.Log
import com.dijkstra.pathfinder.data.dto.BeaconPosition
import com.dijkstra.pathfinder.data.dto.Point
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver
import com.lemmingapex.trilateration.TrilaterationFunction
import org.altbeacon.beacon.Beacon
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt


private const val TAG = "Util_SSAFY"
fun roundToTwoDecimalPlace(number: Double): Double {
    return round(number * 100) / 100
}

fun getDistance(p1: Point, p2: Point): Double {
    return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2) + (p1.z - p2.z).pow(2))
}

fun myDistance(txPower: Int, rssi: Double): Double {
    if (rssi == 0.0) {
        return -1.0
    }
    val ten = 10.0

    val divider = if (rssi < 70.0) {
        16.0
    } else if (rssi >= 70.0 && rssi < 80) {
        35.0
    } else {
        45.0
    }
    val power = (abs(rssi) - abs(txPower)) / (10 * divider)
    val result = roundToTwoDecimalPlace(ten.pow(power))

    return result
}

fun trilateration(beacons: List<Beacon>, allBeaconList: List<BeaconPosition>): DoubleArray {
    val positions = mutableListOf<DoubleArray>()
    val distances = mutableListOf<Double>()
    Log.d(TAG, "trilateration: $beacons")
    // 전체 비콘 리스트를 맵으로
    val beaconPositionMap = allBeaconList.associateBy { it.id }

    beacons.forEach { beacon ->
        // Use the map to get the corresponding Coordinate for the beacon ID
        val tempCoord = beaconPositionMap[beacon.id3.toInt()] ?: run {
            Log.d(TAG, "No matching beacon position found for ID: ${beacon.id3.toInt()}")
            return@forEach
        }

        positions.add(doubleArrayOf(tempCoord.x, tempCoord.y, tempCoord.z))
        distances.add(beacon.distance)
    }
    val solver = NonLinearLeastSquaresSolver(
        TrilaterationFunction(
            positions.toTypedArray(),
            distances.toDoubleArray()
        ), LevenbergMarquardtOptimizer()
    )

    return try {
        solver.solve().point.toArray()
    } catch (e: Exception) {
        Log.e(TAG, "trilateration: $e", e)
        doubleArrayOf(-9999.9, -9999.9, -9999.9)
    }
}