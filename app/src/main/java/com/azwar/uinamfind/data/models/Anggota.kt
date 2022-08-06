package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Anggota(
    val created_at: String?,
    val from_id: String?,
    val id: String?,
    val jabatan: String?,
    val kategori: String?,
    val status: String?,
    val tahun_jabatan: String?,
    val tanggal_penindakan: String?,
    val updated_at: String?,
    val user_id: String?
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(from_id)
        parcel.writeString(id)
        parcel.writeString(jabatan)
        parcel.writeString(kategori)
        parcel.writeString(status)
        parcel.writeString(tahun_jabatan)
        parcel.writeString(tanggal_penindakan)
        parcel.writeString(updated_at)
        parcel.writeString(user_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Anggota> {
        override fun createFromParcel(parcel: Parcel): Anggota {
            return Anggota(parcel)
        }

        override fun newArray(size: Int): Array<Anggota?> {
            return arrayOfNulls(size)
        }
    }
}