package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Foto(
    val created_at: String?,
    val deskripsi: String?,
    val from_id: String?,
    val id: String?,
    val judul: String?,
    val kategori: String?,
    val nama_foto: String?,
    val updated_at: String?,
    val uploader_id: String?
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(deskripsi)
        parcel.writeString(from_id)
        parcel.writeString(id)
        parcel.writeString(judul)
        parcel.writeString(kategori)
        parcel.writeString(nama_foto)
        parcel.writeString(updated_at)
        parcel.writeString(uploader_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Foto> {
        override fun createFromParcel(parcel: Parcel): Foto {
            return Foto(parcel)
        }

        override fun newArray(size: Int): Array<Foto?> {
            return arrayOfNulls(size)
        }
    }
}