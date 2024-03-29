package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Perusahaan(
    val created_at: String?,
    val deskripsi: String?,
    val foto: String?,
    val id: String?,
    val industri: String?,
    val jenis_perusahaan: String?,
    val lokasi: String?,
    val nama: String?,
    val recruiter_id: String?,
    val slug: String?,
    val status: String?,
    val tagline: String?,
    val tahun_berdiri: String?,
    val telpon: String?,
    val ukuran_kariawan: String?,
    val updated_at: String?,
    val email: String?,
    val alamat: String?,
    val url_profil: String?
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
        parcel.writeString(foto)
        parcel.writeString(id)
        parcel.writeString(industri)
        parcel.writeString(jenis_perusahaan)
        parcel.writeString(lokasi)
        parcel.writeString(nama)
        parcel.writeString(recruiter_id)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(tagline)
        parcel.writeString(tahun_berdiri)
        parcel.writeString(telpon)
        parcel.writeString(ukuran_kariawan)
        parcel.writeString(updated_at)
        parcel.writeString(email)
        parcel.writeString(alamat)
        parcel.writeString(url_profil)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Perusahaan> {
        override fun createFromParcel(parcel: Parcel): Perusahaan {
            return Perusahaan(parcel)
        }

        override fun newArray(size: Int): Array<Perusahaan?> {
            return arrayOfNulls(size)
        }
    }
}