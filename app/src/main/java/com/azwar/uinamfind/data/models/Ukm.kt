package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Ukm(
    val nama_ukm: String?,
    val kategori: String?,
    val tahun_berdiri: String?,
    val deskripsi: String?,
    val status_konfirmasi: String?,
    val admin: String?,
    val kontak: String?,
    val email: String?,
    val alamat: String?,
    val created_at: String?,
    val foto: String?,
    val id: String?,
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
        parcel.writeString(nama_ukm)
        parcel.writeString(kategori)
        parcel.writeString(tahun_berdiri)
        parcel.writeString(deskripsi)
        parcel.writeString(status_konfirmasi)
        parcel.writeString(admin)
        parcel.writeString(kontak)
        parcel.writeString(email)
        parcel.writeString(alamat)
        parcel.writeString(created_at)
        parcel.writeString(foto)
        parcel.writeString(id)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ukm> {
        override fun createFromParcel(parcel: Parcel): Ukm {
            return Ukm(parcel)
        }

        override fun newArray(size: Int): Array<Ukm?> {
            return arrayOfNulls(size)
        }
    }
}