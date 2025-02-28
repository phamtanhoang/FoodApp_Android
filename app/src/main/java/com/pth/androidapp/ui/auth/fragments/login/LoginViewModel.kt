package com.pth.androidapp.ui.auth.fragments.login

import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): BaseViewModel() {

}