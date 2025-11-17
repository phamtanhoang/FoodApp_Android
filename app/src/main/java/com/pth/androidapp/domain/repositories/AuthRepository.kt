package com.pth.androidapp.domain.repositories

import com.pth.androidapp.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User

    suspend fun register(email: String, password: String): User

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}
