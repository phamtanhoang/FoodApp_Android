package com.pth.androidapp.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.pth.androidapp.base.network.NetworkResult
import com.pth.androidapp.data.models.imageSlide.ImageSlide
import com.pth.androidapp.data.repositories.ImageSlideRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageSlideRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ImageSlideRepository {

    companion object {
        private const val COLLECTION_IMAGE_SLIDES = "image_slides"
        private const val TAG = "ImageSlideRepo"
    }


    override suspend fun initializeImageSlides(imageSlides: List<ImageSlide>) {
        try {
            // Batch write để tối ưu performance
            val batch = firestore.batch()
            imageSlides.forEach { imageSlide ->
                val docRef = firestore.collection(COLLECTION_IMAGE_SLIDES).document(imageSlide.id)
                batch.set(docRef, imageSlide)
            }

            // Commit batch
            batch.commit().await()

        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAllImageSlides(): Flow<NetworkResult<List<ImageSlide>>> = flow {
        emit(NetworkResult.Loading)

        try {

            val slides = firestore.collection(COLLECTION_IMAGE_SLIDES)
                .get()
                .await()
                .toObjects(ImageSlide::class.java)

            emit(NetworkResult.Success(slides))

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Unknown error occurred"))
        }
    }

}