package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Lamaran(
    val created_at: String?,
    val dokumen_lamaran: String?,
    val email_pelamar: String?,
    val id: String?,
    val jenis_dokumen: String?,
    val loker_id: String?,
    val mahasiswa_id: String?,
    val pesan: String?,
    val recruiter_id: String?,
    val status_lamaran: String?,
    val tanggal: String?,
    val telpon_pelamar: String?,
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
        parcel.writeString(created_at)
        parcel.writeString(dokumen_lamaran)
        parcel.writeString(email_pelamar)
        parcel.writeString(id)
        parcel.writeString(jenis_dokumen)
        parcel.writeString(loker_id)
        parcel.writeString(mahasiswa_id)
        parcel.writeString(pesan)
        parcel.writeString(recruiter_id)
        parcel.writeString(status_lamaran)
        parcel.writeString(tanggal)
        parcel.writeString(telpon_pelamar)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lamaran> {
        override fun createFromParcel(parcel: Parcel): Lamaran {
            return Lamaran(parcel)
        }

        override fun newArray(size: Int): Array<Lamaran?> {
            return arrayOfNulls(size)
        }
    }
}