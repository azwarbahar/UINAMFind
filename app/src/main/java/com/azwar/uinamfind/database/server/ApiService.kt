package com.azwar.uinamfind.database.server

import com.azwar.uinamfind.data.response.Responses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


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

}