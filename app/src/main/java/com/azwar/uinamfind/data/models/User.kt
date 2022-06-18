package com.azwar.uinamfind.data.models

data class User(
    val alamat: String,
    val created_at: String,
    val email: String,
    val email_uinam: String,
    val fakultas: String,
    val foto: Any,
    val foto_sampul: Any,
    val id: String,
    val jenis_kelamin: String,
    val jurusan: String,
    val lokasi: String,
    val nama_belakang: String,
    val nama_depan: String,
    val nim: String,
    val password: String,
    val status_aktif: String,
    val status_akun: String,
    val status_kemahasiswaan: String,
    val suku: String,
    val tahun_masuk: String,
    val tanggal_lahir: String,
    val telpon: String,
    val tempat_lahir: String,
    val tentang_user: String,
    val updated_at: String
)