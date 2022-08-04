package com.azwar.uinamfind.data.models

import android.os.Parcel
import android.os.Parcelable

data class LembagaKampus (
    val admin: String?,
    val cakupan_lembaga: String?,
    val created_at: String?,
    val deskripsi: String?,
    val fakultas: String?,
    val foto: String?,
    val id: String?,
    val jenis_lembaga: String?,
    val jurusan: String?,
    val nama: String?,
    val slug: String?,
    val status: String?,
    val tahun_berdiri: String?,
    val updated_at: String?
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
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(admin)
        parcel.writeString(cakupan_lembaga)
        parcel.writeString(created_at)
        parcel.writeString(deskripsi)
        parcel.writeString(fakultas)
        parcel.writeString(foto)
        parcel.writeString(id)
        parcel.writeString(jenis_lembaga)
        parcel.writeString(jurusan)
        parcel.writeString(nama)
        parcel.writeString(slug)
        parcel.writeString(status)
        parcel.writeString(tahun_berdiri)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LembagaKampus> {
        override fun createFromParcel(parcel: Parcel): LembagaKampus {
            return LembagaKampus(parcel)
        }

        override fun newArray(size: Int): Array<LembagaKampus?> {
            return arrayOfNulls(size)
        }
    }
}