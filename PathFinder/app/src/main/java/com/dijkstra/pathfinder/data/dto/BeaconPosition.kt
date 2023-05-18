package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BeaconPosition(
    val id: Int = 0,
    @SerializedName("beaconX")
    val x: Double,
    @SerializedName("beaconY")
    val y: Double,
    @SerializedName("beaconZ")
    val z: Double
) : Parcelable