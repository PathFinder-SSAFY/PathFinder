package com.dijkstra.pathfinder.data.dto

import android.os.Parcel
import android.os.Parcelable

data class NavigationResponse(
    val nodes: List<Point>?,
    val steps: List<Path>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Point) ?: emptyList(),
        parcel.createTypedArrayList(Path) ?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(nodes)
        parcel.writeTypedList(steps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavigationResponse> {
        override fun createFromParcel(parcel: Parcel): NavigationResponse {
            return NavigationResponse(parcel)
        }

        override fun newArray(size: Int): Array<NavigationResponse?> {
            return arrayOfNulls(size)
        }
    }
}
