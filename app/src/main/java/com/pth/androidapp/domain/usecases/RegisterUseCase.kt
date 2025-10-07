package com.pth.androidapp.domain.usecases

import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        return authRepository.register(email, password)
    }
}

