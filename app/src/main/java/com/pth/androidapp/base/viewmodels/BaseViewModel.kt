package com.pth.androidapp.base.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    fun <T> launch(block: suspend () -> T) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                // Xử lý lỗi toàn cục nếu cần
            }
        }
    }

}
