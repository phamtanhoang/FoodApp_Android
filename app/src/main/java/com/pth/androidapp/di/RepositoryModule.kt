package com.pth.androidapp.di

import com.pth.androidapp.data.repositories.AuthRepository
import com.pth.androidapp.data.repositories.JsonPlaceHolderRepository
import com.pth.androidapp.data.repositories.impl.AuthRepositoryImpl
import com.pth.androidapp.data.repositories.impl.JsonPlaceHolderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindJsonPlaceHolderRepository(
        jsonPlaceHolderRepositoryImpl: JsonPlaceHolderRepositoryImpl
    ): JsonPlaceHolderRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}