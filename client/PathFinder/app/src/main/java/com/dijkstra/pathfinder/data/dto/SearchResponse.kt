package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SearchResponse @JvmOverloads constructor(
    var responseData: MutableList<String> = mutableListOf()
) : Parcelable