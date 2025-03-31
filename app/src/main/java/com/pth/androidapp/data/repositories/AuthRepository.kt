package com.pth.androidapp.data.repositories

import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.auth.LoginResponse
import com.pth.androidapp.data.models.auth.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Flow<NetworkResult<LoginResponse>>

    fun register(
        email: String,
        password: String,
    ): Flow<NetworkResult<RegisterResponse>>

    fun logout(): Flow<NetworkResult<String>>

    fun checkLoggedInStatus(): Flow<NetworkResult<Boolean>>
}