package com.azwar.uinamfind.data.response

import com.azwar.uinamfind.data.models.User

data class LoginUserResponse(
    val `data`: User,
    val kode: String,
    val role: String
)