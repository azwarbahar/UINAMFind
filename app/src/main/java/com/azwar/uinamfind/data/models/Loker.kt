package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Loker(
    val created_at: String?,
    val deskripsi: String?,
    val gaji_max: String?,
    val gaji_min: String?,
    val gaji_tersedia: String?,
    val id: String?,
    val jenis_pekerjaan: String?,
    val jobdesk: String?,
    val lamar_mudah: String?,
    val link_lamar: String?,
    val lokasi: String?,
    val perusahaan: String?,
    val perusahaan_id: String?,
    val posisi: String?,
    val recruiter_id: String?,
    val slug: String?,
    val status: String?,
    val sumber_loker: String?,
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
        parcel.writeString(gaji_max)
        parcel.writeString(gaji_min)
        parcel.writeString(gaji_tersedia)
        parcel.writeString(id)
        parcel.writeString(jenis_pekerjaan)
        parcel.writeString(jobdesk)
        parcel.writeString(lamar_mudah)
        parcel.writeString(link_lamar)
        parcel.writeString(lokasi)
        parcel.writeString(perusahaan)
        parcel.writeString(perusahaan_id)
        parcel.writeString(posisi)
        parcel.writeString(recruiter_id)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(sumber_loker)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Loker> {
        override fun createFromParcel(parcel: Parcel): Loker {
            return Loker(parcel)
        }

        override fun newArray(size: Int): Array<Loker?> {
            return arrayOfNulls(size)
        }
    }
}