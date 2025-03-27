package com.pth.androidapp.base.network

sealed class NetworkResult<out T> {
    object Idle : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}