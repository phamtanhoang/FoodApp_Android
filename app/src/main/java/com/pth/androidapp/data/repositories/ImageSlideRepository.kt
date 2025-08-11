package com.pth.androidapp.data.repositories

import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.imageSlide.ImageSlide
import kotlinx.coroutines.flow.Flow

interface ImageSlideRepository {

    suspend fun initializeImageSlides(imageSlides: List<ImageSlide>)

    fun getAllImageSlides(): Flow<NetworkResult<List<ImageSlide>>>
}