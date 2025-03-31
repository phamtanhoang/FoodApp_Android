package com.pth.androidapp.ui.auth.fragments.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pth.androidapp.R
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.common.TextFieldState
import com.pth.androidapp.common.Utils
import com.pth.androidapp.data.models.auth.LoginResponse
import com.pth.androidapp.data.preferences.UserPreferences
import com.pth.androidapp.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    val email = MutableLiveData(TextFieldState())
    val password = MutableLiveData(TextFieldState())
    val rememberMe = MutableLiveData(false)

    private val _loginResult = MutableLiveData<NetworkResult<LoginResponse>>(NetworkResult.Idle)
    val loginResult: LiveData<NetworkResult<LoginResponse>> = _loginResult

    init {
        loadSavedCredentials()
    }

    private fun loadSavedCredentials() {
        if (userPreferences.getRememberMe()) {
            email.value = TextFieldState(userPreferences.getEmail() ?: "")
            password.value = TextFieldState(userPreferences.getPassword() ?: "")
            rememberMe.value = true
        }
    }

    fun login() {
        launch {
            if (!validateForm()) return@launch

            authRepository.login(
                email = email.value?.text?.trim() ?: "",
                password = password.value?.text?.trim() ?: "",
                rememberMe = rememberMe.value ?: false
            ).collect { result ->
                _loginResult.value = result
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        email.value?.let {
            val error = when {
                it.text.isNullOrBlank() -> context.getString(R.string.email_not_empty)
                !Utils.validateEmail(it.text!!) -> context.getString(R.string.email_not_valid)
                else -> null
            }

            email.value = it.copy(error = error)
            if (error != null) isValid = false
        }

        password.value?.let {
            val error = when {
                it.text.isNullOrBlank() -> context.getString(R.string.password_not_empty)
                !Utils.validatePassword(it.text!!) -> context.getString(R.string.password_not_valid)
                else -> null
            }

            password.value = it.copy(error = error)
            if (error != null) isValid = false
        }

        return isValid
    }

}
