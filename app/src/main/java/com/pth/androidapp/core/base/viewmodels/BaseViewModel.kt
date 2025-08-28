package com.pth.androidapp.core.base.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pth.androidapp.core.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<T> : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<T>>(UiState.Idle)
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    protected fun execute(block: suspend () -> T) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = block()
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unexpected error occurred"
                _uiState.value = UiState.Error(errorMessage)
            }
        }
    }

    protected fun setUiState(newState: UiState<T>) {
        _uiState.value = newState
    }
}