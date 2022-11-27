package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class RoomChat(
    val created_at: String?,
    val from_user: String?,
    val id: String?,
    val to_user: String?,
    val updated_at: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(from_user)
        parcel.writeString(id)
        parcel.writeString(to_user)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomChat> {
        override fun createFromParcel(parcel: Parcel): RoomChat {
            return RoomChat(parcel)
        }

        override fun newArray(size: Int): Array<RoomChat?> {
            return arrayOfNulls(size)
        }
    }
}