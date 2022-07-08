package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class PendidikanMahasiswa(
    val id: String?,
    val pendidikan: String?,
    val nama_tempat: String?,
    val gelar: String?,
    val jurusan: String?,
    val tanggal_masuk: String?,
    val tanggal_berakhir: String?,
    val status_pendidikan: String?,
    val slug_pendidikan: String?,
    val user_id: String?,
    val created_at: String?,
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
        parcel.writeString(id)
        parcel.writeString(pendidikan)
        parcel.writeString(nama_tempat)
        parcel.writeString(gelar)
        parcel.writeString(jurusan)
        parcel.writeString(tanggal_masuk)
        parcel.writeString(tanggal_berakhir)
        parcel.writeString(status_pendidikan)
        parcel.writeString(slug_pendidikan)
        parcel.writeString(user_id)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PendidikanMahasiswa> {
        override fun createFromParcel(parcel: Parcel): PendidikanMahasiswa {
            return PendidikanMahasiswa(parcel)
        }

        override fun newArray(size: Int): Array<PendidikanMahasiswa?> {
            return arrayOfNulls(size)
        }
    }
}