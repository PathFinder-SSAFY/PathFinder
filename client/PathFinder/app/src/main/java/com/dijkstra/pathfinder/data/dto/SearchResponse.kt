package com.dijkstra.pathfinder.data.dto

import androidx.annotation.Keep


@Keep
data class SearchResponse @JvmOverloads constructor(
    var data: List<String> = emptyList()
) : java.io.Serializable