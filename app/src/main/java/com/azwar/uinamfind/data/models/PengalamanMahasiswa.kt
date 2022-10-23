package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class PengalamanMahasiswa(
    val created_at: String?,
    val deskripsi: String?,
    val id: String?,
    val jenis_pengalaman: String?,
    val lokasi_tempat: String?,
    val judul: String?,
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(deskripsi)
        parcel.writeString(id)
        parcel.writeString(jenis_pengalaman)
        parcel.writeString(lokasi_tempat)
        parcel.writeString(judul)
        parcel.writeString(nama_tempat)
        parcel.writeString(slug_pengalaman)
        parcel.writeString(status_pengalaman)
        parcel.writeString(tanggal_berakhir)
        parcel.writeString(tanggal_mulai)
        parcel.writeString(updated_at)
        parcel.writeString(user_id)
    }

    override fun describeContents(): Int {
        return 0
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