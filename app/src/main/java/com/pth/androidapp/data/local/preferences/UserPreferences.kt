package com.pth.androidapp.data.local.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_UID = "USER_UID"
        const val KEY_USER_EMAIL = "USER_EMAIL"
        const val KEY_LOGIN_EMAIL = "KEY_LOGIN_EMAIL"
        const val KEY_LOGIN_PASSWORD = "KEY_LOGIN_PASSWORD"
        const val KEY_REMEMBER_ME = "KEY_REMEMBER_ME"
    }

    fun saveUserInfo(uid: String, email: String) {
        prefs.edit { putString(KEY_USER_UID, uid); putString(KEY_USER_EMAIL, email) }
    }

    fun clearAll() {
        prefs.edit { clear() }
    }

    fun saveCredentials(email: String, password: String, rememberMe: Boolean) {
        prefs.edit {
            putString(KEY_LOGIN_EMAIL, email)
            putString(KEY_LOGIN_PASSWORD, password)
            putBoolean(KEY_REMEMBER_ME, rememberMe)
        }
    }

    fun clearCredentials() {
        prefs.edit {
            remove(KEY_LOGIN_EMAIL)
            remove(KEY_LOGIN_PASSWORD)
            putBoolean(KEY_REMEMBER_ME, false)
        }
    }

    fun getEmail(): String = prefs.getString(KEY_LOGIN_EMAIL, "") ?: ""
    fun getPassword(): String = prefs.getString(KEY_LOGIN_PASSWORD, "") ?: ""
    fun getRememberMe(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)
}