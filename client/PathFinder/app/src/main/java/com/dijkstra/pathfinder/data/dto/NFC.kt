package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NFC @JvmOverloads constructor(
    var beaconList: List<Beacon>?,
    var floorsNumber: List<String>?,
    var mapImageUrl: List<String>?
) : Parcelable