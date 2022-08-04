package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Sosmed(
    val created_at: String?,
    val from_id: String?,
    val id: String?,
    val kategori: String?,
    val nama_sosmed: String?,
    val status: String?,
    val updated_at: String?,
    val url_sosmed: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(from_id)
        parcel.writeString(id)
        parcel.writeString(kategori)
        parcel.writeString(nama_sosmed)
        parcel.writeString(status)
        parcel.writeString(updated_at)
        parcel.writeString(url_sosmed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sosmed> {
        override fun createFromParcel(parcel: Parcel): Sosmed {
            return Sosmed(parcel)
        }

        override fun newArray(size: Int): Array<Sosmed?> {
            return arrayOfNulls(size)
        }
    }
}