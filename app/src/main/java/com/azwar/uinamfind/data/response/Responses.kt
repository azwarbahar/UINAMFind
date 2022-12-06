package com.azwar.uinamfind.data.response

import com.azwar.uinamfind.data.models.*

class Responses {

    data class LoginUserResponse(
        val `data`: User,
        val kode: String,
        val pesan: String,
        val role: String
    )

    data class LoginRecruiterResponse(
        val `recruiter_data`: List<Recruiter>,
        val `data`: Recruiter,
        val `result_recruiter`: Recruiter,
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

    data class ResponseSearch(
        val `search_mahasiswa_data`: List<SearchMahasiswa>?,
        val `search_loker_data`: List<Loker>?,
        val `search_beasiswa_data`: List<Beasiswa>?,
        val `search_magang_data`: List<Magang>?,
        val `search_lembaga_data`: List<LembagaKampus>?,
        val `search_ukm_data`: List<Ukm>?,
        val `search_organisasi_data`: List<Organisasi>?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseChatting(
        val `room_data`: List<RoomChat>?,
        val `result_room`: RoomChat?,
        val `chat_data`: List<Chatting>?,
        val `result_chat`: Chatting?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseMahasiswa(
        val `mahasiswa_data`: List<User>?,
        val `result_mahasiswa`: User?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseInformasi(
        val `informasi_data`: List<Informasi>?,
        val `result_informasi`: Informasi?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseRecruiter(
        val `recruiter_data`: List<Recruiter>?,
        val `result_recruiter`: Recruiter?,
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

    data class ResponseMagang(
        val `magang_data`: List<Magang>?,
        val `result_magang`: Magang?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseBeasiswa(
        val `beasiswa_data`: List<Beasiswa>?,
        val `result_beasiswa`: Beasiswa?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseLamaran(
        val `lamaran_data`: List<Lamaran>?,
        val `result_lamaran`: Lamaran?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponsePerusahaan(
        val `perusahaan_data`: List<Perusahaan>?,
        val `result_perusahaan`: Perusahaan?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseLembaga(
        val `lembaga_data`: List<LembagaKampus>?,
        val `result_lembaga`: LembagaKampus?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseSosmed(
        val `sosmed_data`: List<Sosmed>?,
        val `result_sosmed`: Sosmed?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseKegiatan(
        val `kegiatan_data`: List<Kegiatan>?,
        val `result_kegiatan`: Kegiatan?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseOrganisasi(
        val `organisasi_data`: List<Organisasi>?,
        val `result_organisasi`: Organisasi?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseUkm(
        val `ukm_data`: List<Ukm>?,
        val `result_ukm`: Ukm?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseMarket(
        val `market_data`: List<Market>?,
        val `result_market`: Market?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseAnggota(
        val `anggota_data`: List<Anggota>?,
        val `result_anggota`: Anggota?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseFoto(
        val `foto_data`: List<Foto>?,
        val `result_foto`: Foto?,
        val kode: String?,
        val pesan: String?
    )

}