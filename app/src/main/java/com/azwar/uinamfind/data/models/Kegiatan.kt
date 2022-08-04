package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Kegiatan (
    val created_at: String?,
    val deskripsi: String?,
    val foto: String?,
    val from_id: String?,
    val id: String?,
    val kategori: String?,
    val nama: String?,
    val slug: String?,
    val tanggal: String?,
    val tempat: String?,
    val updated_at: String?
) : Parcelable {
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
        parcel.writeString(created_at)
        parcel.writeString(deskripsi)
        parcel.writeString(foto)
        parcel.writeString(from_id)
        parcel.writeString(id)
        parcel.writeString(kategori)
        parcel.writeString(nama)
        parcel.writeString(slug)
        parcel.writeString(tanggal)
        parcel.writeString(tempat)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Kegiatan> {
        override fun createFromParcel(parcel: Parcel): Kegiatan {
            return Kegiatan(parcel)
        }

        override fun newArray(size: Int): Array<Kegiatan?> {
            return arrayOfNulls(size)
        }
    }
}