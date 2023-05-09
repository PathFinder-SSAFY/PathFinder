package com.dijkstra.pathfinder.data.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.GsonBuilder

data class UserCameraInfoDto(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var azimuth: Float = 0.0f,
    var pitch: Float = 0.0f,
    var roll: Float = 0.0f
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel : Parcel, flags: Int) {
        parcel.writeFloat(x)
        parcel.writeFloat(y)
        parcel.writeFloat(z)
        parcel.writeFloat(azimuth)
        parcel.writeFloat(pitch)
        parcel.writeFloat(roll)
    }

    companion object CREATOR : Parcelable.Creator<UserCameraInfoDto> {
        override fun createFromParcel(parcel: Parcel): UserCameraInfoDto {
            return UserCameraInfoDto(parcel)
        }

        override fun newArray(size: Int): Array<UserCameraInfoDto?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return GsonBuilder().create().toJson(this, UserCameraInfoDto::class.java)
    }

}
