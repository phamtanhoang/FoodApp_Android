package com.pth.androidapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.pth.androidapp.data.local.preferences.UserPreferences
import com.pth.androidapp.data.mappers.toDomainUser
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.InvalidCredentialsException
import com.pth.androidapp.domain.repositories.UserAlreadyExistsException
import com.pth.androidapp.domain.repositories.UserNotFoundException
import com.pth.androidapp.domain.repositories.WeakPasswordException
import com.pth.androidapp.domain.repositories.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String, rememberMe: Boolean): User {
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Authentication failed, user is null.")
            if (rememberMe) {
                userPreferences.saveCredentials(email, password, true)
            } else {
                userPreferences.clearCredentials()
            }

            userPreferences.saveUserInfo(firebaseUser.uid, firebaseUser.email ?: "")

            return firebaseUser.toDomainUser()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException()
                is FirebaseAuthInvalidUserException -> UserNotFoundException()
                else -> e
            }
        }
    }

    override suspend fun register(email: String, password: String): User {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Registration failed, user is null.")
            return firebaseUser.toDomainUser()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthUserCollisionException -> UserAlreadyExistsException()
                is FirebaseAuthWeakPasswordException -> WeakPasswordException()
                else -> e
            }
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        userPreferences.clearAll()
    }

    override suspend fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

}