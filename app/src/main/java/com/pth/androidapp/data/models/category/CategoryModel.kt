package com.pth.androidapp.data.models.category

import com.pth.androidapp.base.models.BaseModel

data class CategoryModel(
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
) : BaseModel()
