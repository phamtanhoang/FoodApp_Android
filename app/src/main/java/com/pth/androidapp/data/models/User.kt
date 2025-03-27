package com.pth.androidapp.data.models

import com.pth.androidapp.data.models.login.LoginResponse

data class User(
    val id: String,
    val email: String,
    val name: String
)

//fun LoginResponse.toUser() = user