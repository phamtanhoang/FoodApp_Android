package com.pth.androidapp.data.models.post

class PostResponse(
    val userId: Int?,
    val id: Int?,
    val title: String?,
    val body: String?
) {
    fun toPost(): Post {
        return Post(
            userId = userId,
            id = id,
            title = title,
            body = body
        )
    }
}