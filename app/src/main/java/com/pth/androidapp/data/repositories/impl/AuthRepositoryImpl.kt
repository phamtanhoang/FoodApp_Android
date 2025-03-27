package com.pth.androidapp.data.repositories.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.login.LoginResponse
import com.pth.androidapp.data.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun login(email: String, password: String): Flow<NetworkResult<LoginResponse>> = flow {
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user =
                authResult.user ?: throw Exception("Authentication succeeded but user is null")

            val loginResponse = LoginResponse(
                email = user.email ?: "",
                userId = user.uid
            )

            emit(NetworkResult.Success(loginResponse))
        } catch (e: Exception) {
            throw e
        }
    }
}