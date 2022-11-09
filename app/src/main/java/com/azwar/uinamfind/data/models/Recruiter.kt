package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class Recruiter(
    val created_at: String?,
    val email: String?,
    val foto: String?,
    val foto_sampul: String?,
    val id: String?,
    val id_perusahaan: String?,
    val jabatan: String?,
    val jenis_kelamin: String?,
    val lokasi: String?,
    val motto: String?,
    val nama: String?,
    val nama_perusahaan: String?,
    val password: String?,
    val status: String?,
    val tanggal_lahir: String?,
    val telpon: String?,
    val tempat_lahir: String?,
    val updated_at: String?,
    val username: String?,
    val verifed_email: String?,
    val verifed_telpon: String?
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(email)
        parcel.writeString(foto)
        parcel.writeString(foto_sampul)
        parcel.writeString(id)
        parcel.writeString(id_perusahaan)
        parcel.writeString(jabatan)
        parcel.writeString(jenis_kelamin)
        parcel.writeString(lokasi)
        parcel.writeString(motto)
        parcel.writeString(nama)
        parcel.writeString(nama_perusahaan)
        parcel.writeString(password)
        parcel.writeString(status)
        parcel.writeString(tanggal_lahir)
        parcel.writeString(telpon)
        parcel.writeString(tempat_lahir)
        parcel.writeString(updated_at)
        parcel.writeString(username)
        parcel.writeString(verifed_email)
        parcel.writeString(verifed_telpon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recruiter> {
        override fun createFromParcel(parcel: Parcel): Recruiter {
            return Recruiter(parcel)
        }

        override fun newArray(size: Int): Array<Recruiter?> {
            return arrayOfNulls(size)
        }
    }
}