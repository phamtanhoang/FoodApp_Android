package com.pth.androidapp.core.common

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<out T>(val data: T) : UiState<T>()

    data class Error(val message: String, val code: String? = null) : UiState<Nothing>()

}

inline fun <T, R> UiState<T>.fold(
    onIdle: () -> R,
    onLoading: () -> R,
    onSuccess: (data: T) -> R,
    onError: (message: String, code: String?) -> R
): R {
    return when (this) {
        is UiState.Idle -> onIdle()
        is UiState.Loading -> onLoading()
        is UiState.Success -> onSuccess(this.data)
        is UiState.Error -> onError(this.message, this.code)
    }
}

inline fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> {
    return when (this) {
        is UiState.Idle -> UiState.Idle
        is UiState.Loading -> UiState.Loading
        is UiState.Success -> UiState.Success(transform(data))
        is UiState.Error -> UiState.Error(message, code)
    }
}

inline fun <T> UiState<T>.onSuccess(action: (data: T) -> Unit): UiState<T> {
    if (this is UiState.Success) action(data)
    return this
}

inline fun <T> UiState<T>.onError(action: (message: String, code: String?) -> Unit): UiState<T> {
    if (this is UiState.Error) action(message, code)
    return this
}

inline fun <T> UiState<T>.onLoading(action: () -> Unit): UiState<T> {
    if (this is UiState.Loading) action()
    return this
}

inline fun <T> UiState<T>.onIdle(action: () -> Unit): UiState<T> {
    if (this is UiState.Idle) action()
    return this
}