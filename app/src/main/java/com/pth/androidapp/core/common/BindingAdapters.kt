package com.pth.androidapp.core.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.MutableStateFlow

@BindingAdapter("stateFlowText")
fun bindStateFlowText(editText: TextInputEditText, state: MutableStateFlow<TextFieldState>?) {
    if (state == null) return
    if (editText.tag != state) {
        editText.setText(state.value.text)
        editText.tag = state
    }
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (state.value.text != s.toString()) {
                state.value = state.value.copy(text = s.toString())
            }
        }
        override fun afterTextChanged(s: Editable?) {}
    })
}

@BindingAdapter("visible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}