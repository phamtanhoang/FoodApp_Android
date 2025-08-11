package com.pth.androidapp.data.models.imageSlide

import com.pth.androidapp.base.models.BaseModel

data class ImageSlide(
    val imageUrl: String = "",
    val description: String = "",
) : BaseModel()