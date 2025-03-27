package com.pth.androidapp.ui.auth.fragments.register

import androidx.lifecycle.MutableLiveData
import com.pth.androidapp.base.viewmodels.BaseViewModel
import com.pth.androidapp.common.TextFieldState

class RegisterViewModel: BaseViewModel() {

    val email = MutableLiveData(TextFieldState(text = ""))
    val password = MutableLiveData(TextFieldState(text = ""))
    val confirmPassword = MutableLiveData(TextFieldState(text = ""))

    fun register() {}
}