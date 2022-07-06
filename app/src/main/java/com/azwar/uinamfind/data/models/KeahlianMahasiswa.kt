package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class KeahlianMahasiswa(
    val created_at: String?,
    val id: String?,
    val level_skill: String?,
    val nama_skill: String?,
    val updated_at: String?,
    val user_id: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.writeString(id)
        parcel.writeString(level_skill)
        parcel.writeString(nama_skill)
        parcel.writeString(updated_at)
        parcel.writeString(user_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KeahlianMahasiswa> {
        override fun createFromParcel(parcel: Parcel): KeahlianMahasiswa {
            return KeahlianMahasiswa(parcel)
        }

        override fun newArray(size: Int): Array<KeahlianMahasiswa?> {
            return arrayOfNulls(size)
        }
    }
}