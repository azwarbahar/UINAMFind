package com.azwar.uinamfind.database.server

import com.azwar.uinamfind.data.response.Responses
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    // FOTO
    @GET("foto/getFoto.php")
    fun getFoto(
        @Query("kategori") kategori: String?,
        @Query("from_id") from_id: String?
    ): Call<Responses.ResponseFoto>?

    // ANGGOTA
    @GET("anggota/getAnggota.php")
    fun getAnggota(
        @Query("kategori") kategori: String?,
        @Query("from_id") from_id: String?,
        @Query("user_id") user_id: String?
    ): Call<Responses.ResponseAnggota>?


    // ORGANISASI
    @GET("organisasi/getOrganisasi.php")
    fun getOrganisasi(): Call<Responses.ResponseOrganisasi>?


    // UKM
    @GET("ukm/getUkm.php")
    fun getUkm(): Call<Responses.ResponseOrganisasi>?


    // KEGIATAN
    @GET("kegiatan/getKegiatanKategori.php")
    fun getKegiatanKategori(
        @Query("from_id") from_id: String?,
        @Query("kategori") kategori: String?
    ): Call<Responses.ResponseKegiatan>?


    // SOSMED
    @GET("sosmed/getSosmedKategori.php")
    fun getSosmedKategori(
        @Query("from_id") from_id: String?,
        @Query("kategori") kategori: String?
    ): Call<Responses.ResponseSosmed>?


    // LEMBAGA
    @GET("lembaga/getLembagaId.php")
    fun getLembagaId(
        @Query("id") id: String?
    ): Call<Responses.ResponseLembaga>?

    @GET("lembaga/getLembagaCakupan.php")
    fun getLembagaCakupan(
        @Query("cakupan") cakupan: String?
    ): Call<Responses.ResponseLembaga>?

    @GET("lembaga/getLembagaFakultas.php")
    fun getLembagaFakultas(
        @Query("fakultas") fakultas: String?
    ): Call<Responses.ResponseLembaga>?

    // LAMARAN
    @GET("lamaran/getLamaranStatusRecruiterId.php")
    fun getLamaranStatusRecruiterId(
        @Query("status_lamaran") status_lamaran: String?,
        @Query("recruiter_id") recruiter_id: String?
    ): Call<Responses.ResponseLamaran>?

    // LAMARAN ID
    @GET("lamaran/getLamaranId.php")
    fun getLamaranId(
        @Query("id") id: String?
    ): Call<Responses.ResponseLamaran>?

    // LAMARAN PER LOKER
    @GET("lamaran/getLamaranLokerRecruiter.php")
    fun getLamaranLokerRecruiter(
        @Query("loker_id") loker_id: String?
    ): Call<Responses.ResponseLamaran>?

    @FormUrlEncoded
    @POST("lamaran/updateLamaranStatus.php")
    fun updateLamaranStatus(
        @Field("lamaran_id") lamaran_id: String?,
        @Field("status_lamaran") status_lamaran: String?
    ): Call<Responses.ResponseLamaran>?


    // LOKER
    @GET("loker/getLoker.php")
    fun getLoker(
        @Query("halaman") halaman: String?
    ): Call<Responses.ResponseLoker>?

    // ADD LOKER
    @FormUrlEncoded
    @POST("loker/addLoker.php")
    fun addLoker(
        @Field("posisi") posisi: String?,
        @Field("jobdesk") jobdesk: String?,
        @Field("deskripsi") deskripsi: String?,
        @Field("lokasi") lokasi: String?,
        @Field("jenis_pekerjaan") jenis_pekerjaan: String?,
        @Field("gaji_tersedia") gaji_tersedia: String?,
        @Field("gaji_max") gaji_max: String?,
        @Field("gaji_min") gaji_min: String?,
        @Field("recruiter_id") recruiter_id: String?,
        @Field("perusahaan_id") perusahaan_id: String?,
        @Field("lamar_mudah") lamar_mudah: String?,
        @Field("link_lamar") link_lamar: String?
    ): Call<Responses.ResponseLoker>?

    // UPDATE LOKER
    @FormUrlEncoded
    @POST("loker/updateLoker.php")
    fun updateLoker(
        @Field("id") id: String?,
        @Field("posisi") posisi: String?,
        @Field("jobdesk") jobdesk: String?,
        @Field("deskripsi") deskripsi: String?,
        @Field("lokasi") lokasi: String?,
        @Field("jenis_pekerjaan") jenis_pekerjaan: String?,
        @Field("gaji_tersedia") gaji_tersedia: String?,
        @Field("gaji_max") gaji_max: String?,
        @Field("gaji_min") gaji_min: String?,
        @Field("lamar_mudah") lamar_mudah: String?,
        @Field("link_lamar") link_lamar: String?
    ): Call<Responses.ResponseLoker>?

    // LOKER ID
    @GET("loker/deleteLoker.php")
    fun deleteLoker(
        @Query("id") id: String?
    ): Call<Responses.ResponseLoker>?

    // LOKER ID
    @GET("loker/getLokerId.php")
    fun getLokerId(
        @Query("id") id: String?
    ): Call<Responses.ResponseLoker>?

    // Loker Recruiter ID
    @GET("loker/getLokerRecruiterId.php")
    fun getLokerRecruiterId(
        @Query("recruiter_id") recruiter_id: String?
    ): Call<Responses.ResponseLoker>?

    // PERUSAHAAN
    @GET("perusahaan/getPerusahaanId.php")
    fun getPerusahaanId(
        @Query("id") id: String?
    ): Call<Responses.ResponsePerusahaan>?

    // UPDATE PERUSAHAAN
    @FormUrlEncoded
    @POST("perusahaan/updatePerusahaan.php")
    fun updatePerusahaan(
        @Field("id") id: String?,
        @Field("nama") nama: String?,
        @Field("url_profil") url_profil: String?,
        @Field("industri") industri: String?,
        @Field("ukuran_kariawan") ukuran_kariawan: String?,
        @Field("telpon") telpon: String?,
        @Field("email") email: String?,
        @Field("tahun_berdiri") tahun_berdiri: String?,
        @Field("deskripsi") deskripsi: String?,
        @Field("alamat") alamat: String?,
        @Field("lokasi") lokasi: String?,
        @Field("recruiter_id") recruiter_id: String?
    ): Call<Responses.ResponsePerusahaan>?

    // UPDATE PERUSHAAN WITH PHOTO
    @Multipart
    @POST("perusahaan/updatePerusahaan.php")
    fun updatePerusahaanPhoto(
        @Part("id") id: RequestBody?,
        @Part("nama") nama: RequestBody?,
        @Part("url_profil") url_profil: RequestBody?,
        @Part("industri") industri: RequestBody?,
        @Part("ukuran_kariawan") ukuran_kariawan: RequestBody?,
        @Part("telpon") telpon: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("tahun_berdiri") tahun_berdiri: RequestBody?,
        @Part("deskripsi") deskripsi: RequestBody?,
        @Part("alamat") alamat: RequestBody?,
        @Part("lokasi") lokasi: RequestBody?,
        @Part("recruiter_id") recruiter_id: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<Responses.ResponsePerusahaan>?


    @GET("login.php")
    fun login(
        @Query("email_uinam") email_uinam: String?,
        @Query("password") password: String?
    ): Call<Responses.LoginUserResponse>?

    // Login Recruiter
    @GET("login-recruiter.php")
    fun loginRecruiter(
        @Query("email") email: String?,
        @Query("password") password: String?
    ): Call<Responses.LoginRecruiterResponse>?

    //Recruiter ID
    @GET("recruiter/getRecruiterId.php")
    fun getRecruiterId(
        @Query("id") id: String?
    ): Call<Responses.LoginRecruiterResponse>?

    // UPDATE RECRUITER
    @FormUrlEncoded
    @POST("recruiter/updateRecruiter.php")
    fun updateRecruiter(
        @Field("id") id: String?,
        @Field("nama") nama: String?,
        @Field("jenis_kelamin") jenis_kelamin: String?,
        @Field("tempat_lahir") tempat_lahir: String?,
        @Field("tanggal_lahir") tanggal_lahir: String?,
        @Field("telpon") telpon: String?,
        @Field("email") email: String?,
        @Field("motto") motto: String?,
        @Field("lokasi") lokasi: String?
    ): Call<Responses.ResponseRecruiter>?

    //Update
    @Multipart
    @POST("recruiter/updatePhotoRecruiter.php")
    fun updatePhotoRecruiter(
        @Part("ket") ket: RequestBody?,
        @Part("id") id: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<Responses.ResponseRecruiter>?


    //Mahasiswa
    @GET("mahasiswa/getMahasiswaId.php")
    fun getMahasiswaID(
        @Query("id") id: String?
    ): Call<Responses.ResponseMahasiswa>?

    // mahasiswa se fakultas
    @GET("mahasiswa/getMahasiswaFakultas.php")
    fun getMahasiswaFakultas(
        @Query("fakultas") fakultas: String?,
        @Query("id") id: String?
    ): Call<Responses.ResponseMahasiswa>?

    // mahasiswa Random
    @GET("mahasiswa/getMahasiswaRandom.php")
    fun getMahasiswaRandom(
        @Query("id") id: String?
    ): Call<Responses.ResponseMahasiswa>?

    //Mahasiswa Update Terbaru
    @GET("mahasiswa/getMahasiswaNewUpdate.php")
    fun getMahasiswaNewUpdate(
        @Query("id") id: String?
    ): Call<Responses.ResponseMahasiswa>?

    @GET("mahasiswa/checkProfilMahasiswa.php")
    fun checkProfilMahasiswa(
        @Query("id") id: String?
    ): Call<Responses.ResponseCheckProfilMahasiswa>?

    //update
    @FormUrlEncoded
    @POST("mahasiswa/updateMahasiswa.php")
    fun updateMahasiswa(
        @Field("id") id: String?,
        @Field("nama_depan") nama_depan: String?,
        @Field("nama_belakang") nama_belakang: String?,
        @Field("alamat") alamat: String?,
        @Field("jenis_kelamin") jenis_kelamin: String?,
        @Field("lokasi") lokasi: String?,
        @Field("tanggal_lahir") tanggal_lahir: String?
    ): Call<Responses.ResponseMahasiswa>?

    //Update
    @Multipart
    @POST("mahasiswa/updatePhoto.php")
    fun updatePhoto(
        @Part("ket") ket: RequestBody?,
        @Part("id") id: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<Responses.ResponseMahasiswa>?


    @FormUrlEncoded
    @POST("mahasiswa/updateTentangSaya.php")
    fun updateTentangSaya(
        @Field("tentang_user") tentang_user: String?,
        @Field("id") id: String?
    ): Call<Responses.ResponseMahasiswa>?

    //Motto
    @GET("mahasiswa/motto/getMottoId.php")
    fun getMottoId(
        @Query("user_id") user_id: String?
    ): Call<Responses.ResponseMotto>?

    // Update Motto
    @FormUrlEncoded
    @POST("mahasiswa/motto/updateMottoUser.php")
    fun updateMottoUser(
        @Field("id") id: String?,
        @Field("user_id") user_id: String?,
        @Field("motto_profesional") motto_profesional: String?
    ): Call<Responses.ResponseMotto>?

    //ORGANISASI
    @GET("mahasiswa/organisasi/getOrganisasiUser.php")
    fun getOrganisasiUser(
        @Query("user_id") user_id: String?
    ): Call<Responses.ResponseOrganisasiMahasiswa>?

    @GET("mahasiswa/organisasi/deleteOrganisasiUser.php")
    fun deleteOrganisasiUser(
        @Query("id") id: String?
    ): Call<Responses.ResponseOrganisasiMahasiswa>?

    @FormUrlEncoded
    @POST("mahasiswa/organisasi/addOrganisasiUser.php")
    fun addOrganisasiUser(
        @Field("nama_organisasi") nama_organisasi: String?,
        @Field("jabatan") jabatan: String?,
        @Field("tanggal_mulai") tanggal_mulai: String?,
        @Field("tanggal_berakhir") tanggal_berakhir: String?,
        @Field("status_organisasi_user") status_organisasi_user: String?,
        @Field("kedudukan_organisasi") kedudukan_organisasi: String?,
        @Field("organisasi_id") organisasi_id: String?,
        @Field("deskripsi") deskripsi: String?,
        @Field("user_id") user_id: String?
    ): Call<Responses.ResponseOrganisasiMahasiswa>?

    @FormUrlEncoded
    @POST("mahasiswa/organisasi/updateOrgnaisasiUser.php")
    fun updateOrgnaisasiUser(
        @Field("nama_organisasi") nama_organisasi: String?,
        @Field("jabatan") jabatan: String?,
        @Field("tanggal_mulai") tanggal_mulai: String?,
        @Field("tanggal_berakhir") tanggal_berakhir: String?,
        @Field("status_organisasi_user") status_organisasi_user: String?,
        @Field("kedudukan_organisasi") kedudukan_organisasi: String?,
        @Field("deskripsi") deskripsi: String?,
        @Field("user_id") user_id: String?,
        @Field("id") id: String?
    ): Call<Responses.ResponseOrganisasiMahasiswa>?


    // PENGALAMAN
    //get
    @GET("mahasiswa/pengalaman/getPengalamanUser.php")
    fun getPengalamanUser(
        @Query("user_id") user_id: String?
    ): Call<Responses.ResponsePengalamanMahasiswa>?

    //add
    @FormUrlEncoded
    @POST("mahasiswa/pengalaman/addPengalamanUser.php")
    fun addPengalamanUser(
        @Field("nama") nama: String?,
        @Field("jenis_pengalaman") jenis_pengalaman: String?,
        @Field("nama_tempat") nama_tempat: String?,
        @Field("lokasi_tempat") lokasi_tempat: String?,
        @Field("tanggal_mulai") tanggal_mulai: String?,
        @Field("tanggal_berakhir") tanggal_berakhir: String?,
        @Field("status_pengalaman") status_pengalaman: String?,
        @Field("deskripsi") deskripsi: String?,
        @Field("user_id") user_id: String?
    ): Call<Responses.ResponsePengalamanMahasiswa>?

    //delete
    @GET("mahasiswa/pengalaman/deletePengalamanUser.php")
    fun deletePengalamanUser(
        @Query("id") id: String?
    ): Call<Responses.ResponsePengalamanMahasiswa>?

    //update
    @FormUrlEncoded
    @POST("mahasiswa/pengalaman/updatePengalamanUser.php")
    fun updatePengalamanUser(
        @Field("id") id: String?,
        @Field("user_id") user_id: String?,
        @Field("nama") nama: String?,
        @Field("jenis_pengalaman") jenis_pengalaman: String?,
        @Field("nama_tempat") nama_tempat: String?,
        @Field("lokasi_tempat") lokasi_tempat: String?,
        @Field("tanggal_mulai") tanggal_mulai: String?,
        @Field("tanggal_berakhir") tanggal_berakhir: String?,
        @Field("status_pengalaman") status_pengalaman: String?,
        @Field("deskripsi") deskripsi: String?
    ): Call<Responses.ResponsePengalamanMahasiswa>?


    // KEAHLIAN
    //get
    @GET("mahasiswa/keahlian/getKeahlianUser.php")
    fun getKeahlianUser(
        @Query("user_id") user_id: String?
    ): Call<Responses.ResponseKeahlianMahasiswa>?

    //add
    @FormUrlEncoded
    @POST("mahasiswa/keahlian/addKeahlianUser.php")
    fun addKeahlianUser(
        @Field("user_id") user_id: String?,
        @Field("nama_skill") nama_skill: String?,
        @Field("level_skill") level_skill: String?
    ): Call<Responses.ResponseKeahlianMahasiswa>?

    //delete
    @GET("mahasiswa/keahlian/deleteKeahlianUser.php")
    fun deleteKeahlianUser(
        @Query("id") id: String?
    ): Call<Responses.ResponseKeahlianMahasiswa>?

    //update
    @FormUrlEncoded
    @POST("mahasiswa/keahlian/updateKeahlianUser.php")
    fun updateKeahlianUser(
        @Field("id") id: String?,
        @Field("user_id") user_id: String?,
        @Field("nama_skill") nama_skill: String?,
        @Field("level_skill") level_skill: String?
    ): Call<Responses.ResponseKeahlianMahasiswa>?


    // PENDIDIKAN
    //get
    @GET("mahasiswa/pendidikan/getPendidikanUser.php")
    fun getPendidikanUser(
        @Query("user_id") user_id: String?
    ): Call<Responses.ResponsePendidikanMahasiswa>?

    //add
    @FormUrlEncoded
    @POST("mahasiswa/pendidikan/addPendidikanUser.php")
    fun addPendidikanUser(
        @Field("pendidikan") pendidikan: String?,
        @Field("nama_tempat") nama_tempat: String?,
        @Field("jurusan") jurusan: String?,
        @Field("tanggal_masuk") tanggal_masuk: String?,
        @Field("tanggal_berakhir") tanggal_berakhir: String?,
        @Field("status_pendidikan") status_pendidikan: String?,
        @Field("user_id") user_id: String?
    ): Call<Responses.ResponsePendidikanMahasiswa>?

    //delete
    @GET("mahasiswa/pendidikan/deletePendidikanUser.php")
    fun deletePendidikanUser(
        @Query("id") id: String?
    ): Call<Responses.ResponsePendidikanMahasiswa>?

    //update
    @FormUrlEncoded
    @POST("mahasiswa/pendidikan/updatePendidikanUser.php")
    fun updatePendidikanUser(
        @Field("id") id: String?,
        @Field("pendidikan") pendidikan: String?,
        @Field("nama_tempat") nama_tempat: String?,
        @Field("jurusan") jurusan: String?,
        @Field("tanggal_masuk") tanggal_masuk: String?,
        @Field("tanggal_berakhir") tanggal_berakhir: String?,
        @Field("status_pendidikan") status_pendidikan: String?,
        @Field("user_id") user_id: String?
    ): Call<Responses.ResponsePendidikanMahasiswa>?


}