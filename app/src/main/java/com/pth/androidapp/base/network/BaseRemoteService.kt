package com.pth.androidapp.base.network

import android.util.Log
import com.pth.androidapp.ui.auth.fragments.jsonPlaceHolder.JsonPlaceHolderFragment.Companion.JsonPlaceHolderTAG
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
                                code = response.code(),
                                message = response.message()
                            )
                        )
                    }
                } catch (e: Exception) {
                    emit(
                        NetworkResult.Error(
                            code = 400,
                            message = e.message ?: "Something went wrong!"
                        )
                    )
                }
            } ?: emit(NetworkResult.Error(code = 408, message = "Timeout! Please try again."))
        }.flowOn(Dispatchers.IO)
    }
}