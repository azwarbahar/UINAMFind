package com.azwar.uinamfind.data.response

import com.azwar.uinamfind.data.models.Motto
import com.azwar.uinamfind.data.models.OrganisasiMahasiswa
import com.azwar.uinamfind.data.models.User

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

}