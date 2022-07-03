package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class PengalamanMahasiswa(
    val created_at: String?,
    val deskripsi: String?,
    val id: String?,
    val jenis_pengalaman: String?,
    val lokasi_tempat: String?,
    val nama: String?,
    val nama_tempat: String?,
    val slug_pengalaman: String?,
    val status_pengalaman: String?,
    val tanggal_berakhir: String?,
    val tanggal_mulai: String?,
    val updated_at: String?,
    val user_id: String?
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<PengalamanMahasiswa> {
        override fun createFromParcel(parcel: Parcel): PengalamanMahasiswa {
            return PengalamanMahasiswa(parcel)
        }

        override fun newArray(size: Int): Array<PengalamanMahasiswa?> {
            return arrayOfNulls(size)
        }
    }
}