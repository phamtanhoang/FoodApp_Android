package com.pth.androidapp.domain.usecases

import com.pth.androidapp.data.local.preferences.UserPreferences
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): User {

        val user = authRepository.login(email, password)

        if (rememberMe) {
            userPreferences.saveCredentials(email, password, true)
        } else {
            userPreferences.clearCredentials()
        }

        userPreferences.saveUserInfo(user.id, user.email)

        return user
    }
}

