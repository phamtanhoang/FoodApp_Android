package com.pth.androidapp.presentation.auth.fragments.register

import android.content.Context
import com.pth.androidapp.R
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.EMPTY
import com.pth.androidapp.core.common.InputValidator
import com.pth.androidapp.core.common.TextFieldState
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val validator: InputValidator,
    @ApplicationContext private val context: Context
) : BaseViewModel() {
    private val _registerState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val registerState = _registerState.asStateFlow()

    val emailState = MutableStateFlow(TextFieldState(EMPTY.str))
    val passwordState = MutableStateFlow(TextFieldState(EMPTY.str))
    val confirmPasswordState = MutableStateFlow(TextFieldState(EMPTY.str))

    fun onRegisterClicked() {
        if (!validateForm()) return

        execute(_registerState) {
            registerUseCase(
                email = emailState.value.text.trim(),
                password = passwordState.value.text
            )
        }
    }

    private fun validateForm(): Boolean {
        val email = emailState.value.text
        val password = passwordState.value.text
        val confirmPassword = confirmPasswordState.value.text

        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)
        val isConfirmPasswordValid = validateConfirmPassword(password, confirmPassword)

        return isEmailValid && isPasswordValid && isConfirmPasswordValid
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isBlank() -> {
                emailState.update { it.copy(error = context.getString(R.string.email_not_empty)) }
                false
            }
            !validator.validateEmail(email) -> {
                emailState.update { it.copy(error = context.getString(R.string.email_not_valid)) }
                false
            }
            else -> {
                emailState.update { it.copy(error = null) }
                true
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when {
            password.isBlank() -> {
                passwordState.update { it.copy(error = context.getString(R.string.password_not_empty)) }
                false
            }
            !validator.validatePassword(password) -> {
                passwordState.update { it.copy(error = context.getString(R.string.password_not_valid)) }
                false
            }
            else -> {
                passwordState.update { it.copy(error = null) }
                true
            }
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        return when {
            confirmPassword.isBlank() -> {
                confirmPasswordState.update { it.copy(error = context.getString(R.string.password_not_empty)) }
                false
            }
            !validator.validatePassword(confirmPassword) -> {
                confirmPasswordState.update { it.copy(error = context.getString(R.string.password_not_valid)) }
                false
            }
            password != confirmPassword -> {
                confirmPasswordState.update { it.copy(error = context.getString(R.string.password_not_match)) }
                false
            }
            else -> {
                confirmPasswordState.update { it.copy(error = null) }
                true
            }
        }
    }

}