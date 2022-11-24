package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Informasi(
    val cakupan: String?,
    val created_at: String?,
    val gambar: String?,
    val id: String?,
    val judul: String?,
    val penulis_id: String?,
    val pesan: String?,
    val slug: String?,
    val status: String?,
    val tanggal: String?,
    val updated_at: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.writeString(cakupan)
        parcel.writeString(created_at)
        parcel.writeString(gambar)
        parcel.writeString(id)
        parcel.writeString(judul)
        parcel.writeString(penulis_id)
        parcel.writeString(pesan)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(tanggal)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Informasi> {
        override fun createFromParcel(parcel: Parcel): Informasi {
            return Informasi(parcel)
        }

        override fun newArray(size: Int): Array<Informasi?> {
            return arrayOfNulls(size)
        }
    }
}