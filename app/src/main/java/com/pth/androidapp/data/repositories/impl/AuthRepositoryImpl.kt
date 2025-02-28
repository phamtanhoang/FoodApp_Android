package com.pth.androidapp.data.repositories.impl

import com.pth.androidapp.data.repositories.AuthRepository
import com.pth.androidapp.data.services.AuthRemoteService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteService: AuthRemoteService
): AuthRepository {

}