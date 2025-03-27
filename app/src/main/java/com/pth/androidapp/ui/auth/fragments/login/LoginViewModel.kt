package com.pth.androidapp.ui.auth.fragments.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.pth.androidapp.R
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.common.TextFieldState
import com.pth.androidapp.common.Utils
import com.pth.androidapp.data.models.login.LoginResponse
import com.pth.androidapp.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    val email = MutableLiveData(TextFieldState())
    val password = MutableLiveData(TextFieldState())
    val rememberMe = MutableLiveData(false)

    private val _loginResult = MutableLiveData<NetworkResult<LoginResponse>>(NetworkResult.Idle)
    val loginResult: LiveData<NetworkResult<LoginResponse>> = _loginResult

    fun login() {
        launch {
            if (!validateForm()) return@launch

            authRepository.login(
                email = email.value?.text?.trim() ?: "",
                password = password.value?.text?.trim() ?: ""
            )
                .onStart { _loginResult.value = NetworkResult.Loading }
                .catch { cause ->
                    _loginResult.value = NetworkResult.Error(getErrorMessage(cause))
                }
                .collect { result ->
                    _loginResult.value = result
                }
        }
    }

    private fun getErrorMessage(cause: Throwable): String = when (cause) {
        is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.error_invalid_credentials)
        is FirebaseAuthInvalidUserException -> context.getString(R.string.error_user_not_found)
        else -> cause.message ?: context.getString(R.string.error_unknown)
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
