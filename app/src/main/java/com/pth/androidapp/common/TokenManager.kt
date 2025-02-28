package com.pth.androidapp.common

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TokenManager(context: Context) {
    companion object {
        private const val PREF_NAME = "TokenPrefs"
        private const val TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getAccessToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    suspend fun saveAccessToken(token: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
        }
    }

    suspend fun deleteAccessToken() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(TOKEN_KEY).apply()
        }
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply()
        }
    }

    suspend fun deleteRefreshToken() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(REFRESH_TOKEN_KEY).apply()
        }
    }

    suspend fun clearTokens() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().remove(TOKEN_KEY).remove(REFRESH_TOKEN_KEY).apply()
        }
    }
}