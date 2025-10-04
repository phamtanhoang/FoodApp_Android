package com.pth.androidapp.core.common

import android.content.Context
import com.pth.androidapp.R

object FieldValidator {
    fun validateEmail(context: Context, email: String, validator: InputValidator): String? {
        return when {
            email.isBlank() -> context.getString(R.string.email_not_empty)
            !validator.validateEmail(email) -> context.getString(R.string.email_not_valid)
            else -> null
        }
    }

    fun validatePassword(context: Context, password: String, validator: InputValidator): String? {
        return when {
            password.isBlank() -> context.getString(R.string.password_not_empty)
            !validator.validatePassword(password) -> context.getString(R.string.password_not_valid)
            else -> null
        }
    }

    fun validateConfirmPassword(context: Context, password: String, confirmPassword: String, validator: InputValidator): String? {
        return when {
            confirmPassword.isBlank() -> context.getString(R.string.password_not_empty)
            !validator.validatePassword(confirmPassword) -> context.getString(R.string.password_not_valid)
            password != confirmPassword -> context.getString(R.string.password_not_match)
            else -> null
        }
    }
}

