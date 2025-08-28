package com.pth.androidapp.presentation.auth.register

import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.TextFieldState
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<User>() {
    private val _emailState = MutableStateFlow(TextFieldState())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(TextFieldState())
    val passwordState = _passwordState.asStateFlow()

    private val _confirmPasswordState = MutableStateFlow(TextFieldState())
    val confirmPasswordState = _confirmPasswordState.asStateFlow()

    fun onRegisterClicked() {
        if (!validateForm()) return

        execute {
            authRepository.register(
                email = _emailState.value.text,
                password = _passwordState.value.text
            )
        }
    }

    private fun validateForm(): Boolean {
        val email = _emailState.value.text
        val password = _passwordState.value.text
        val confirmPassword = _confirmPasswordState.value.text
        var isValid = true

        if (email.isBlank()) {
            _emailState.update { it.copy(error = "Email không được để trống") }
            isValid = false
        } else {
            _emailState.update { it.copy(error = null) }
        }

        if (password.length < 6) {
            _passwordState.update { it.copy(error = "Mật khẩu phải có ít nhất 6 ký tự") }
            isValid = false
        } else {
            _passwordState.update { it.copy(error = null) }
        }

        if (password != confirmPassword) {
            _confirmPasswordState.update { it.copy(error = "Mật khẩu không khớp") }
            isValid = false
        } else {
            _confirmPasswordState.update { it.copy(error = null) }
        }

        return isValid
    }
}