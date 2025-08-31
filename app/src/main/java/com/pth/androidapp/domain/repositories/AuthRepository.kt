package com.pth.androidapp.domain.repositories

import com.pth.androidapp.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, password: String, rememberMe: Boolean): User

    suspend fun register(email: String, password: String): User

    suspend fun logout()

    suspend fun isLoggedIn(): Boolean
}

class InvalidCredentialsException : Exception("Invalid email or password.")
class UserNotFoundException : Exception("User with the given credentials not found.")
class UserAlreadyExistsException : Exception("A user with this email already exists.")
class WeakPasswordException : Exception("The password is too weak.")