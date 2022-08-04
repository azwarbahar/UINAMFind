package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Organisasi(
    val admin: String?,
    val created_at: String?,
    val deskripsi: String?,
    val foto: String?,
    val foto_sampul: String?,
    val id: String?,
    val kategori: String?,
    val nama_organisasi: String?,
    val slug: String?,
    val status: String?,
    val tahun_berdiri: String?,
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(admin)
        parcel.writeString(created_at)
        parcel.writeString(deskripsi)
        parcel.writeString(foto)
        parcel.writeString(foto_sampul)
        parcel.writeString(id)
        parcel.writeString(kategori)
        parcel.writeString(nama_organisasi)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(tahun_berdiri)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Organisasi> {
        override fun createFromParcel(parcel: Parcel): Organisasi {
            return Organisasi(parcel)
        }

        override fun newArray(size: Int): Array<Organisasi?> {
            return arrayOfNulls(size)
        }
    }
}