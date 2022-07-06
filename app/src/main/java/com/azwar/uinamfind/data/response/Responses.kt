package com.azwar.uinamfind.data.response

import com.azwar.uinamfind.data.models.*

class Responses {

    data class LoginUserResponse(
        val `data`: User,
        val kode: String,
        val pesan: String,
        val role: String
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

}