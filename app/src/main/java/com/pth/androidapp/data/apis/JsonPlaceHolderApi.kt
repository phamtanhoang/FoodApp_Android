package com.pth.androidapp.data.apis

import com.pth.androidapp.data.models.post.PostResponse
import retrofit2.Response
import retrofit2.http.GET

interface JsonPlaceHolderApi {

    @GET("/posts")
    suspend fun getAllPost(): Response<List<PostResponse>>

}