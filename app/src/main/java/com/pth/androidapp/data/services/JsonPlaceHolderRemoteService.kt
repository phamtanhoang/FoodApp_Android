package com.pth.androidapp.data.services

import com.pth.androidapp.base.network.BaseRemoteService
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.apis.JsonPlaceHolderApi
import com.pth.androidapp.data.models.post.PostResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JsonPlaceHolderRemoteService @Inject constructor(
    private val jsonPlaceHolderApi: JsonPlaceHolderApi
) : BaseRemoteService() {

    suspend fun getAllPost(): Flow<NetworkResult<List<PostResponse>>> = apiRequestFlow {
        jsonPlaceHolderApi.getAllPost()
    }

}