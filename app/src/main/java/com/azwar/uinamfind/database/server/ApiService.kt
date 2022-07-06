package com.azwar.uinamfind.database.server

import com.azwar.uinamfind.data.response.Responses
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("login.php")
    fun login(
        @Query("email_uinam") email_uinam: String?,
        @Query("password") password: String?
    ): Call<Responses.LoginUserResponse>?

    //Mahasiswa
    @GET("mahasiswa/getMahasiswaId.php")
    fun getMahasiswaID(
        @Query("id") id: String?
    ): Call<Responses.ResponseMahasiswa>?

    //Motto
    @GET("mahasiswa/motto/getMottoId.php")
    fun getMottoId(
        @Query("user_id") user_id: String?
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


}