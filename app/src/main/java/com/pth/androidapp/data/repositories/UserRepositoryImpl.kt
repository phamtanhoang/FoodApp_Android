package com.pth.androidapp.data.repositories

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pth.androidapp.domain.entities.User
import com.pth.androidapp.domain.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val FIELD_UID = "uid"
        private const val FIELD_EMAIL = "email"
        private const val FIELD_FIRST_NAME = "firstName"
        private const val FIELD_LAST_NAME = "lastName"
        private const val FIELD_AGE = "age"
        private const val FIELD_SEX = "sex"
        private const val FIELD_ADDRESS = "address"
        private const val FILED_PHONE_NUMBER = "phoneNumber"
        private const val FIELD_AVATAR = "avatar"

        private const val FIELD_CREATED_AT = "createdAt"

        private const val FIELD_UPDATED_AT = "updatedAt"
    }

    override suspend fun addUser(user: User) {
        val userDocument = hashMapOf(
            FIELD_UID to user.id,
            FIELD_EMAIL to user.email,
            FIELD_FIRST_NAME to "",
            FIELD_LAST_NAME to "",
            FIELD_AGE to null,
            FIELD_SEX to null,
            FIELD_ADDRESS to "",
            FILED_PHONE_NUMBER to "",
            FIELD_AVATAR to "",
            FIELD_CREATED_AT to FieldValue.serverTimestamp(),
            FIELD_UPDATED_AT to FieldValue.serverTimestamp(),

        )
        firestore.collection(USERS_COLLECTION).document(user.id).set(userDocument).await()
    }
}
