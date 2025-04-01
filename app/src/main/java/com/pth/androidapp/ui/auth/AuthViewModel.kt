package com.pth.androidapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _loginState = MutableLiveData<NetworkResult<Boolean>>()
    val loginState: LiveData<NetworkResult<Boolean>> = _loginState

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        launch {
            authRepository.checkLoggedInStatus().collect { result ->
                _loginState.value = result
            }
        }
    }
}