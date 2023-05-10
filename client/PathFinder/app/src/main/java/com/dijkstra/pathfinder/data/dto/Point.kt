package com.dijkstra.pathfinder.data.dto

import android.os.Parcel
import android.os.Parcelable

data class Point(
    val x: Double,
    val y: Double,
    val z: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(x)
        parcel.writeDouble(y)
        parcel.writeDouble(z)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Point> {
        override fun createFromParcel(parcel: Parcel): Point {
            return Point(parcel)
        }

        override fun newArray(size: Int): Array<Point?> {
            return arrayOfNulls(size)
        }
    }
}
