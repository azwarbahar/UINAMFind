package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Beasiswa(
    val admin_id: String?,
    val batas_tanggal: String?,
    val created_at: String?,
    val deskripsi: String?,
    val foto: String?,
    val id: String?,
    val instansi: String?,
    val judul: String?,
    val link_info: String?,
    val role_admin: String?,
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(admin_id)
        parcel.writeString(batas_tanggal)
        parcel.writeString(created_at)
        parcel.writeString(deskripsi)
        parcel.writeString(foto)
        parcel.writeString(id)
        parcel.writeString(instansi)
        parcel.writeString(judul)
        parcel.writeString(link_info)
        parcel.writeString(role_admin)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Beasiswa> {
        override fun createFromParcel(parcel: Parcel): Beasiswa {
            return Beasiswa(parcel)
        }

        override fun newArray(size: Int): Array<Beasiswa?> {
            return arrayOfNulls(size)
        }
    }
}