package com.dijkstra.pathfinder.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchTest @JvmOverloads constructor(
    var filteringSearch: String? = ""
) : Parcelable
