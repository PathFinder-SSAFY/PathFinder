package com.dijkstra.pathfinder.data.dto

import android.os.Parcel
import android.os.Parcelable
import kotlin.math.roundToInt

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

    operator fun plus(b: Point): Point {
        return Point(
            this.x + b.x,
            this.y + b.y,
            this.z + b.z

        )
    }

    operator fun minus(b: Point): Point {
        return Point(
            this.x - b.x,
            this.y - b.y,
            this.z - b.z
        )
    }

    fun round(): Point = Point(
        x.roundToInt().toDouble(),
        y.roundToInt().toDouble(),
        z.roundToInt().toDouble()
    )

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
