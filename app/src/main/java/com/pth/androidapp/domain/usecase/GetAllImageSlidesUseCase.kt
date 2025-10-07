package com.pth.androidapp.domain.usecase

import com.pth.androidapp.domain.models.ImageSlide
import com.pth.androidapp.domain.repositories.ImageSlideRepository
import javax.inject.Inject

class GetAllImageSlidesUseCase @Inject constructor(
    private val repository: ImageSlideRepository
) {
    suspend operator fun invoke(): List<ImageSlide> = repository.getAllImageSlides()
}

