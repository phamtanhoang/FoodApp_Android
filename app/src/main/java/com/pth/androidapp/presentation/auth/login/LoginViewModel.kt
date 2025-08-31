package com.pth.androidapp.presentation.auth.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.TextFieldState
import com.pth.androidapp.data.local.preferences.UserPreferences
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : BaseViewModel<User>() {

    private val _emailState = MutableStateFlow(TextFieldState())
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow(TextFieldState())
    val passwordState = _passwordState.asStateFlow()

    private val _rememberMeState = MutableStateFlow(false)
    val rememberMeState = _rememberMeState.asStateFlow()

    init {
        loadSavedCredentials()
    }

    private fun loadSavedCredentials() {
        viewModelScope.launch {
            if (userPreferences.getRememberMe()) {
                _emailState.value = TextFieldState(userPreferences.getEmail())
                _passwordState.value = TextFieldState(userPreferences.getPassword())
                _rememberMeState.value = true
            }
        }
    }

    fun onLoginClicked() {
        if (!validateForm()) return

        execute {
            authRepository.login(
                email = _emailState.value.text,
                password = _passwordState.value.text,
                rememberMe = _rememberMeState.value
            )
        }
    }

    private fun validateForm(): Boolean {
        val email = _emailState.value.text
        val password = _passwordState.value.text
        var isValid = true

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailState.update { it.copy(error = "Email không hợp lệ") }
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

        return isValid
    }
}