package com.pth.androidapp.data.repositories.impl

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.pth.androidapp.R
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.auth.LoginResponse
import com.pth.androidapp.data.models.auth.RegisterResponse
import com.pth.androidapp.data.preferences.UserPreferences
import com.pth.androidapp.data.repositories.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences,
    @ApplicationContext private val context: Context
) : AuthRepository {

    override fun login(
        email: String,
        password: String,
        rememberMe: Boolean
    ): Flow<NetworkResult<LoginResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
                ?: throw Exception(context.getString(R.string.authentication_succeeded_but_user_is_null))

            val loginResponse = LoginResponse(
                email = user.email ?: "",
                userId = user.uid
            )

            userPreferences.saveUserInfo(user.uid, user.email ?: "")
            userPreferences.saveLoginCredentials(email, password, rememberMe)

            emit(NetworkResult.Success(loginResponse))
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.error_invalid_credentials)
                is FirebaseAuthInvalidUserException -> context.getString(R.string.error_user_not_found)
                else -> context.getString(R.string.login_failed)
            }
            emit(NetworkResult.Error(errorMessage))
        }
    }

    override fun register(
        email: String,
        password: String
    ): Flow<NetworkResult<RegisterResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val user = authResult.user
                ?: throw Exception(context.getString(R.string.authentication_succeeded_but_user_is_null))

            val registerResponse = RegisterResponse(
                email = user.email ?: "",
                userId = user.uid,
                message = context.getString(R.string.registration_success)
            )

            emit(NetworkResult.Success(registerResponse))
        } catch (e: Exception) {

            val errorMessage = when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    context.getString(R.string.email_not_valid)

                is FirebaseAuthInvalidUserException ->
                    context.getString(R.string.user_already_exists)

                else -> context.getString(R.string.registration_failed)
            }
            emit(NetworkResult.Error(errorMessage))
        }
    }

    override fun logout(): Flow<NetworkResult<String>> = flow {
        try {
            firebaseAuth.signOut()
            userPreferences.clearUserInfo()

            emit(NetworkResult.Success(context.getString(R.string.logout_success)))
        } catch (e: Exception) {
            emit(NetworkResult.Error(context.getString(R.string.logout_failed)))
        }
    }

    override fun checkLoggedInStatus(): Flow<NetworkResult<Boolean>> = flow {
        try {
            val isLoggedIn = userPreferences.isLoggedIn() && firebaseAuth.currentUser != null

            emit(NetworkResult.Success(isLoggedIn))
        } catch (e: Exception) {
            emit(
                NetworkResult.Error(
                    context.getString(R.string.failed_to_check_login_status)
                )
            )
        }
    }
}