package com.azwar.uinamfind.data.response

import com.azwar.uinamfind.data.models.*

class Responses {

    data class LoginUserResponse(
        val `data`: User,
        val kode: String,
        val pesan: String,
        val role: String
    )

    data class ResponseCheckProfilMahasiswa(
        val kode: String?,
        val pesan: String?,
        val foto: String?,
        val tentang_user: String?,
        val pendidikan: Int?,
        val keahlian: Int?,
        val pengalaman: Int?,
        val organisasi: Int?
    )

    data class ResponseMahasiswa(
        val `mahasiswa_data`: List<User>?,
        val `result_mahasiswa`: User?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseMotto(
        val `result_motto`: Motto?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseOrganisasiMahasiswa(
        val `organisasi_data`: List<OrganisasiMahasiswa>?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponsePengalamanMahasiswa(
        val `pengalaman_data`: List<PengalamanMahasiswa>?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseKeahlianMahasiswa(
        val `keahlian_data`: List<KeahlianMahasiswa>?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponsePendidikanMahasiswa(
        val `pendidikan_data`: List<PendidikanMahasiswa>?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseLoker(
        val `loker_data`: List<Loker>?,
        val `result_loker`: Loker?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponsePerusahaan(
        val `perusahaan_data`: List<Perusahaan>?,
        val `result_perusahaan`: Perusahaan?,
        val kode: String?,
        val pesan: String?
    )

}