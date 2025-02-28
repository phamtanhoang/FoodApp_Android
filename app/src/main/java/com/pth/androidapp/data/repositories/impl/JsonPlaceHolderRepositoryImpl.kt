package com.pth.androidapp.data.repositories.impl

import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.post.Post
import com.pth.androidapp.data.repositories.JsonPlaceHolderRepository
import com.pth.androidapp.data.services.JsonPlaceHolderRemoteService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JsonPlaceHolderRepositoryImpl @Inject constructor(
    private val jsonPlaceHolderRemoteService: JsonPlaceHolderRemoteService
): JsonPlaceHolderRepository {
    override suspend fun getAllPost(): Flow<NetworkResult<List<Post>>> {
        return jsonPlaceHolderRemoteService.getAllPost().map { networkResult ->
            when (networkResult) {
                is NetworkResult.Success -> {
                    val posts = networkResult.data.map { it.toPost() }
                    NetworkResult.Success(posts)
                }

                is NetworkResult.Error -> {
                    NetworkResult.Error(
                        code = networkResult.code,
                        message = networkResult.message
                    )
                }

                is NetworkResult.Loading -> {
                    NetworkResult.Loading
                }
            }
        }
    }
}