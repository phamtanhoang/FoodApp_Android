package com.pth.androidapp.domain.repositories

import com.pth.androidapp.domain.entities.User

interface UserRepository {
    suspend fun addUser(user: User)
}
