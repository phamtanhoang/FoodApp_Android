package com.pth.androidapp.domain.usecases

import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import com.pth.androidapp.domain.repositories.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        val user = authRepository.register(email, password)
        userRepository.addUser(user)
        authRepository.logout()
        return user
    }
}
