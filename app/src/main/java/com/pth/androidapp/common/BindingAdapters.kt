package com.pth.androidapp.common

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorText")
fun TextInputLayout.setErrorText(errorMessage: String?) {
    error = errorMessage
    isErrorEnabled = errorMessage != null
}

