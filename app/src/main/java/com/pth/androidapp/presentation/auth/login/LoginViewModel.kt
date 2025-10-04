package com.pth.androidapp.presentation.auth.login

import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.base.viewmodels.BaseViewModel
import com.pth.androidapp.core.common.InputValidator
import com.pth.androidapp.core.common.TextFieldState
import com.pth.androidapp.core.common.UiState
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.AuthRepository
import com.pth.androidapp.data.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
    private val validator: InputValidator
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    val emailState = MutableStateFlow(TextFieldState())
    val passwordState = MutableStateFlow(TextFieldState())
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
            authRepository.login(
                email = emailState.value.text.trim(),
                password = passwordState.value.text,
                rememberMe = rememberMeState.value
            )
        }
    }

    private fun validateForm(): Boolean {
//        val emailResult = validator.validateEmail(emailState.value.text)
//        val passwordResult = validator.validatePassword(passwordState.value.text)
//
//        emailState.update { it.copy(error = (emailResult as? ValidationResult.Failure)) }
//        passwordState.update { it.copy(error = (passwordResult as? ValidationResult.Failure)) }
//
//        return emailResult is ValidationResult.Success && passwordResult is ValidationResult.Success
        return false;
    }
}