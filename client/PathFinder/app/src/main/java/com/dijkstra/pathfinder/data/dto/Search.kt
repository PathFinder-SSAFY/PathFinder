package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Search(
    val id: Int,
    val facilityType: String,
    @SerializedName("hisCount")
    val count: Int
) : Parcelable
