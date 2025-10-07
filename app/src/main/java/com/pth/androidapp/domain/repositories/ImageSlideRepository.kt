package com.pth.androidapp.domain.repositories

import com.pth.androidapp.domain.models.ImageSlide

interface ImageSlideRepository {
    suspend fun getAllImageSlides(): List<ImageSlide>
}