package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Beacon @JvmOverloads constructor(
    var beaconX: Double,
    var beaconY: Double,
    var beaconZ: Double,
    var id: Int
) : Parcelable {
}