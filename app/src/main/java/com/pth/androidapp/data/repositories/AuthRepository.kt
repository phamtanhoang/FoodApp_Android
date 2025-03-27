package com.pth.androidapp.data.repositories

import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.login.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<NetworkResult<LoginResponse>>
}