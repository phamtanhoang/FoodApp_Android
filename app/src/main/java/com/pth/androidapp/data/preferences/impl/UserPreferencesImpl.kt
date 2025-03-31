package com.pth.androidapp.data.preferences.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import com.pth.androidapp.data.preferences.UserPreferences

@Singleton
class UserPreferencesImpl @Inject constructor(@ApplicationContext context: Context) :
    UserPreferences {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCES_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun saveLoginCredentials(email: String, password: String, rememberMe: Boolean) {
        if (rememberMe) {
            sharedPreferences.edit() {
                putString(KEY_EMAIL, email)
                    .putString(KEY_PASSWORD, password)
                    .putBoolean(KEY_REMEMBER_ME, true)
            }
        } else {
            clearLoginCredentials()
        }
    }

    override fun saveUserInfo(userId: String, email: String) {
        sharedPreferences.edit() {
            putString(KEY_USER_ID, userId)
                .putString(KEY_USER_EMAIL, email)
        }
    }

    override fun clearLoginCredentials() {
        sharedPreferences.edit() {
            remove(KEY_EMAIL)
                .remove(KEY_PASSWORD)
                .putBoolean(KEY_REMEMBER_ME, false)
        }
    }

    override fun clearUserInfo() {
        sharedPreferences.edit() {
            remove(KEY_USER_ID)
                .remove(KEY_USER_EMAIL)
        }
    }

    override fun getEmail(): String? = sharedPreferences.getString(KEY_EMAIL, null)

    override fun getPassword(): String? = sharedPreferences.getString(KEY_PASSWORD, null)

    override fun getRememberMe(): Boolean = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)

    override fun getUserId(): String? = sharedPreferences.getString(KEY_USER_ID, null)

    override fun getUserEmail(): String? = sharedPreferences.getString(KEY_USER_EMAIL, null)

    override fun isLoggedIn(): Boolean = !getUserId().isNullOrBlank()

    companion object {
        private const val PREFERENCES_NAME = "user_preferences"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
    }
}
