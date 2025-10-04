package com.pth.androidapp.core.common

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InputValidator @Inject constructor() {

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun validatePassword(password: String): Boolean {
        return !(password.length < 8 || !password.contains(Regex("[A-Z]")) || !password.contains(Regex("[a-z]")) || !password.contains(
            Regex("[0-9]")
        ) || password.matches(Regex("^[a-zA-Z0-9]+\$")))
    }
}