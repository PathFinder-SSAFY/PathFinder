package com.dijkstra.pathfinder.util

import android.util.Log
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver
import com.lemmingapex.trilateration.TrilaterationFunction
import org.altbeacon.beacon.Beacon
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round


private const val TAG = "Util_SSAFY"
fun roundToTwoDecimalPlace(number: Double): Double {
    return round(number * 100) / 100
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

fun trilateration(beacons: List<Beacon>): DoubleArray {
    val positions = mutableListOf<DoubleArray>()
    val distances = mutableListOf<Double>()
    Log.d(TAG, "trilateration: $beacons")
    // 전체 비콘 리스트를 맵으로
    val beaconPositionMap = beaconPositionList.associateBy { it.id }

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