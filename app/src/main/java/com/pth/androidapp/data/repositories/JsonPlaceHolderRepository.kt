package com.pth.androidapp.data.repositories

import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.post.Post
import kotlinx.coroutines.flow.Flow

interface JsonPlaceHolderRepository {
    suspend fun getAllPost(): Flow<NetworkResult<List<Post>>>
}