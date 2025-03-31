package com.pth.androidapp.di

import com.pth.androidapp.data.preferences.UserPreferences
import com.pth.androidapp.data.preferences.impl.UserPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferences(
        userPreferencesImpl: UserPreferencesImpl
    ): UserPreferences
}