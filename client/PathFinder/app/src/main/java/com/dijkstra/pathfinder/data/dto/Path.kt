package com.dijkstra.pathfinder.data.dto

import android.os.Parcel
import android.os.Parcelable
import com.dijkstra.pathfinder.util.Constant
import com.google.gson.GsonBuilder

data class Path(
    val node: Point,
    var distance: Double,
    val direction: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Point::class.java.classLoader) ?: Point(0.0, 0.0, 0.0),
        parcel.readDouble(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(node, flags)
        parcel.writeDouble(distance)
        parcel.writeInt(direction)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return GsonBuilder().create().toJson(this, Path::class.java)

    }

    fun getNextPoint(): Point {
        return when (this.direction) {
            Constant.WEST -> {
                this.node - Point(this.distance, 0.0, 0.0)
            }
            Constant.NORTH -> {
                this.node + Point(0.0, 0.0, this.distance)
            }
            Constant.EAST -> {
                this.node + Point(this.distance, 0.0, 0.0)
            }
            Constant.SOUTH -> {
                this.node - Point(0.0, 0.0, this.distance)
            }
            else -> this.node
        }
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