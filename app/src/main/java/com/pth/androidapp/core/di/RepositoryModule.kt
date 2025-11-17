package com.pth.androidapp.core.di

import com.pth.androidapp.data.repositories.AuthRepositoryImpl
import com.pth.androidapp.data.repositories.ImageSlideRepositoryImpl
import com.pth.androidapp.data.repositories.UserRepositoryImpl
import com.pth.androidapp.domain.repositories.AuthRepository
import com.pth.androidapp.domain.repositories.ImageSlideRepository
import com.pth.androidapp.domain.repositories.UserRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindImageSlideRepository(
        imageSlideRepositoryImpl: ImageSlideRepositoryImpl
    ): ImageSlideRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}