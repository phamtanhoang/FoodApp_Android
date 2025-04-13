package com.pth.androidapp.ui.fragments.home

import android.content.Context
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class GenieViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : BaseViewModel() {
    private val _logoutState = MutableStateFlow<NetworkResult<String>>(NetworkResult.Idle)
    val logoutState: StateFlow<NetworkResult<String>> = _logoutState

    fun logout() {
        launch {
            authRepository.logout().collect { result ->
                _logoutState.value = result
            }
        }
    }
}