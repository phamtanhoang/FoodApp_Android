package com.pth.androidapp.data.local.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserInfo(uid: String, email: String) {
        prefs.edit {
            putString("USER_UID", uid)
            putString("USER_EMAIL", email)
        }
    }

    fun clearAll() {
        prefs.edit { clear() }
    }

}