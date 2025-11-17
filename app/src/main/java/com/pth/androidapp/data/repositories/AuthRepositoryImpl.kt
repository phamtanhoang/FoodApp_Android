package com.pth.androidapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.pth.androidapp.core.utils.ErrorKeys
import com.pth.androidapp.data.local.preferences.UserPreferences
import com.pth.androidapp.data.mappers.toDomainUser
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override suspend fun login(email: String, password: String): User {
        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception(ErrorKeys.SOMETHING_WENT_WRONG)

            return firebaseUser.toDomainUser()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthInvalidCredentialsException -> Exception(ErrorKeys.INVALID_CREDENTIALS)
                is FirebaseAuthInvalidUserException -> Exception(ErrorKeys.USER_NOT_FOUND)
                else -> Exception(ErrorKeys.SOMETHING_WENT_WRONG)
            }
        }
    }

    override suspend fun register(email: String, password: String): User {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception(ErrorKeys.SOMETHING_WENT_WRONG)

            return firebaseUser.toDomainUser()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthUserCollisionException -> Exception(ErrorKeys.USER_ALREADY_EXISTS)
                is FirebaseAuthWeakPasswordException -> Exception(ErrorKeys.WEAK_PASSWORD)
                is FirebaseAuthInvalidCredentialsException -> Exception(ErrorKeys.INVALID_CREDENTIALS)
                else -> Exception(ErrorKeys.SOMETHING_WENT_WRONG)
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
