package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    val alamat: String?,
    val created_at: String?,
    val email: String?,
    val email_uinam: String?,
    val fakultas: String?,
    val foto: String?,
    val foto_sampul: String?,
    val id: String?,
    val jenis_kelamin: String?,
    val jurusan: String?,
    val lokasi: String?,
    val nama_belakang: String?,
    val nama_depan: String?,
    val nim: String?,
    val password: String?,
    val status_aktif: String?,
    val status_akun: String?,
    val status_kemahasiswaan: String?,
    val suku: String?,
    val tahun_masuk: String?,
    val tanggal_lahir: String?,
    val telpon: String?,
    val tempat_lahir: String?,
    val tentang_user: String?,
    val updated_at: String?,
    val username: String?
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(alamat)
        parcel.writeString(created_at)
        parcel.writeString(email)
        parcel.writeString(email_uinam)
        parcel.writeString(fakultas)
        parcel.writeString(foto)
        parcel.writeString(foto_sampul)
        parcel.writeString(id)
        parcel.writeString(jenis_kelamin)
        parcel.writeString(jurusan)
        parcel.writeString(lokasi)
        parcel.writeString(nama_belakang)
        parcel.writeString(nama_depan)
        parcel.writeString(nim)
        parcel.writeString(password)
        parcel.writeString(status_aktif)
        parcel.writeString(status_akun)
        parcel.writeString(status_kemahasiswaan)
        parcel.writeString(suku)
        parcel.writeString(tahun_masuk)
        parcel.writeString(tanggal_lahir)
        parcel.writeString(telpon)
        parcel.writeString(tempat_lahir)
        parcel.writeString(tentang_user)
        parcel.writeString(updated_at)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}