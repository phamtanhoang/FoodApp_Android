package com.pth.androidapp.presentation.auth.fragments.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.R
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.EMPTY
import com.pth.androidapp.core.common.InputValidator
import com.pth.androidapp.core.common.TextFieldState
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.data.local.preferences.UserPreferences
import com.pth.androidapp.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val userPreferences: UserPreferences,
    private val validator: InputValidator,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    val emailState = MutableStateFlow(TextFieldState(EMPTY.str))
    val passwordState = MutableStateFlow(TextFieldState(EMPTY.str))
    val rememberMeState = MutableStateFlow(false)

    init {
        loadSavedCredentials()
    }

    private fun loadSavedCredentials() {
        viewModelScope.launch {
            if (userPreferences.getRememberMe()) {
                emailState.value = TextFieldState(userPreferences.getEmail())
                passwordState.value = TextFieldState(userPreferences.getPassword())
                rememberMeState.value = true
            }
        }
    }

    fun onLoginClicked() {
        if (!validateForm()) return
        execute(_loginState) {
            loginUseCase(
                email = emailState.value.text.trim(),
                password = passwordState.value.text,
                rememberMe = rememberMeState.value
            )
        }
    }

    private fun validateForm(): Boolean {
        val email = emailState.value.text
        val password = passwordState.value.text

        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        return isEmailValid && isPasswordValid
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
}