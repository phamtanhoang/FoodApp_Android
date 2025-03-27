package com.pth.androidapp.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Utils {

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    fun validatePassword(password: String): Boolean {
        return !(password.length < 8 || !password.contains(Regex("[A-Z]")) || !password.contains(Regex("[a-z]")) || !password.contains(
            Regex("[0-9]")
        ) || password.matches(Regex("^[a-zA-Z0-9]+\$")))
    }

    fun EditText.setTextChangeListener(
        textInputLayout: TextInputLayout,
        lifecycleOwner: LifecycleOwner,
        debounceTime: Long = Constants.debounceTime
    ) {
        var debounceJob: Job? = null

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                debounceJob?.cancel()
                debounceJob = lifecycleOwner.lifecycleScope.launch {
                    delay(debounceTime)
                    textInputLayout.isErrorEnabled = false
                    textInputLayout.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }

        addTextChangedListener(textWatcher)

        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                removeTextChangedListener(textWatcher)
            }
        })
    }
}