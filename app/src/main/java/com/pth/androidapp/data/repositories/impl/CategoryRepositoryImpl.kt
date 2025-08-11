package com.pth.androidapp.data.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pth.androidapp.data.models.category.CategoryModel
import com.pth.androidapp.data.repositories.CategoryRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CategoryRepository {

    override suspend fun getCategories(): List<CategoryModel> {
        return try {
            val snapshot = firestore.collection("categories")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(CategoryModel::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getCategoriesFlow(): Flow<List<CategoryModel>> = callbackFlow {
        val listenerRegistration = firestore.collection("categories")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val categories = snapshot.documents.mapNotNull { document ->
                        document.toObject(CategoryModel::class.java)?.copy(id = document.id)
                    }
                    trySend(categories)
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getCategoriesForSlider(limit: Long): List<CategoryModel> {
        return try {
            val snapshot = firestore.collection("categories")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(CategoryModel::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getCategoriesByField(field: String, value: Any): List<CategoryModel> {
        return try {
            val snapshot = firestore.collection("categories")
                .whereEqualTo(field, value)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(CategoryModel::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}