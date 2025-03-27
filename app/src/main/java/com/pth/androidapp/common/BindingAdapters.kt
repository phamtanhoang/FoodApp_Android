package com.pth.androidapp.common

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorMessage")
fun TextInputLayout.setErrorMessage(errorMessage: String?) {
    error = errorMessage
    isErrorEnabled = errorMessage != null
}

