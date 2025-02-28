package com.pth.androidapp.base.network

sealed class NetworkResult<out T: Any> {
    data object Loading : NetworkResult<Nothing>()

    data class Success<out T : Any>(
        val data: T,
    ) : NetworkResult<T>()

    data class Error(
        val code: Int? = null,
        val message: String? = null,
    ) : NetworkResult<Nothing>()
}
