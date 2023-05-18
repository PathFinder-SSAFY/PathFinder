package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchValidResponse(
    val isValid: Boolean,
    val points: Point?
) : Parcelable
