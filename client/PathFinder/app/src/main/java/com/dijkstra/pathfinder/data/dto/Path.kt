package com.dijkstra.pathfinder.data.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.GsonBuilder

data class Path(
    val startPoint: Point,
    val distance: Double,
    val direction: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Point::class.java.classLoader) ?: Point(0.0, 0.0, 0.0),
        parcel.readDouble(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(startPoint, flags)
        parcel.writeDouble(distance)
        parcel.writeInt(direction)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return GsonBuilder().create().toJson(this, Path::class.java)

    }

    companion object CREATOR : Parcelable.Creator<Path> {
        override fun createFromParcel(parcel: Parcel): Path {
            return Path(parcel)
        }

        override fun newArray(size: Int): Array<Path?> {
            return arrayOfNulls(size)
        }
    }
}