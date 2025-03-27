package com.pth.androidapp.base.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

open class BaseRemoteService {
    protected suspend fun <T : Any> apiRequestFlow(
        call: suspend () -> Response<T>
    ): Flow<NetworkResult<T>> {
        return flow {
            emit(NetworkResult.Loading)

            withTimeoutOrNull(20000L) {
                val response = call()
                try {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            emit(NetworkResult.Success(data = data))
                        }
                    } else {
                        emit(
                            NetworkResult.Error(
                                message = response.message()
                            )
                        )
                    }
                } catch (e: Exception) {
                    emit(
                        NetworkResult.Error(
                            message = e.message ?: "Something went wrong!"
                        )
                    )
                }
            } ?: emit(NetworkResult.Error(message = "Timeout! Please try again."))
        }.flowOn(Dispatchers.IO)
    }
}