package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Market(
    val created_at: String?,
    val deskripsi: String?,
    val foto1: String?,
    val foto2: String?,
    val foto3: String?,
    val harga: String?,
    val id: String?,
    val judul: String?,
    val lokasi: String?,
    val nomor_wa: String?,
    val penjual_id: String?,
    val satuan: String?,
    val slug: String?,
    val status: String?,
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(deskripsi)
        parcel.writeString(foto1)
        parcel.writeString(foto2)
        parcel.writeString(foto3)
        parcel.writeString(harga)
        parcel.writeString(id)
        parcel.writeString(judul)
        parcel.writeString(lokasi)
        parcel.writeString(nomor_wa)
        parcel.writeString(penjual_id)
        parcel.writeString(satuan)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Market> {
        override fun createFromParcel(parcel: Parcel): Market {
            return Market(parcel)
        }

        override fun newArray(size: Int): Array<Market?> {
            return arrayOfNulls(size)
        }
    }
}