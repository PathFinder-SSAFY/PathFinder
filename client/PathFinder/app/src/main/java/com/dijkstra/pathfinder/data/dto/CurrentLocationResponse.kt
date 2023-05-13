package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentLocationResponse(
    @SerializedName("resposneData")
    var responseData: String = ""
) : Parcelable