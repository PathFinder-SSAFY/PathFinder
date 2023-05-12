package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SearchResponse @JvmOverloads constructor(
    var data: MutableList<String> = mutableListOf()
) : Parcelable