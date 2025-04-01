package com.pth.androidapp.di


import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.pth.androidapp.data.preferences.UserPreferences
import com.pth.androidapp.data.repositories.AuthRepository
import com.pth.androidapp.data.repositories.impl.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        userPreferences: UserPreferences,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, userPreferences, context)
    }
}