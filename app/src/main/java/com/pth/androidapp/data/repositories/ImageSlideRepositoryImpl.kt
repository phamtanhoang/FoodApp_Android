package com.pth.androidapp.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.pth.androidapp.domain.models.ImageSlide
import com.pth.androidapp.domain.repositories.ImageSlideRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageSlideRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ImageSlideRepository {

    companion object {
        private const val COLLECTION_IMAGE_SLIDES = "image_slides"
    }

    override suspend fun getAllImageSlides(): List<ImageSlide> {
        return firestore.collection(COLLECTION_IMAGE_SLIDES)
            .get()
            .await()
            .toObjects(ImageSlide::class.java)
    }
}