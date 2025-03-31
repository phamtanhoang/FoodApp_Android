package com.pth.androidapp.ui.auth.fragments.register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pth.androidapp.R
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.common.TextFieldState
import com.pth.androidapp.common.Utils
import com.pth.androidapp.data.models.auth.RegisterResponse
import com.pth.androidapp.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {
    val email = MutableLiveData(TextFieldState())
    val password = MutableLiveData(TextFieldState())
    val confirmPassword = MutableLiveData(TextFieldState())

    private val _registerResult = MutableLiveData<NetworkResult<RegisterResponse>>(NetworkResult.Idle)
    val registerResult: LiveData<NetworkResult<RegisterResponse>> = _registerResult

    fun register() {
        launch {
            if (!validateForm()) return@launch

            authRepository.register(
                email = email.value?.text?.trim() ?: "",
                password = password.value?.text?.trim() ?: "",
            ).collect { result ->
                _registerResult.value = result
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

        confirmPassword.value?.let {
            val error = when {
                it.text.isNullOrBlank() -> context.getString(R.string.password_not_empty)
                it.text != password.value?.text -> context.getString(R.string.password_not_match)
                else -> null
            }

            confirmPassword.value = it.copy(error = error)
            if (error != null) isValid = false
        }

        return isValid
    }
}