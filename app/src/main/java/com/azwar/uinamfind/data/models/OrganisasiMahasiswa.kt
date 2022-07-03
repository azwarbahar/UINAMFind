package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class OrganisasiMahasiswa(
    val created_at: String?,
    val deskripsi: String?,
    val id: String?,
    val jabatan: String?,
    val kedudukan_organisasi: String?,
    val nama_organisasi: String?,
    val organisasi_id: String?,
    val slug: String?,
    val status_organisasi_user: String?,
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
        parcel.writeString(jabatan)
        parcel.writeString(kedudukan_organisasi)
        parcel.writeString(nama_organisasi)
        parcel.writeString(organisasi_id)
        parcel.writeString(slug)
        parcel.writeString(status_organisasi_user)
        parcel.writeString(tanggal_berakhir)
        parcel.writeString(tanggal_mulai)
        parcel.writeString(updated_at)
        parcel.writeString(user_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrganisasiMahasiswa> {
        override fun createFromParcel(parcel: Parcel): OrganisasiMahasiswa {
            return OrganisasiMahasiswa(parcel)
        }

        override fun newArray(size: Int): Array<OrganisasiMahasiswa?> {
            return arrayOfNulls(size)
        }
    }
}