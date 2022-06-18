package com.azwar.uinamfind.database.server

import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.LoginUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {

    @GET("login.php")
    suspend fun login(
        @Query("email_uinam") email_uinam: String?
    ): Response<LoginUserResponse?>?

}