package com.pth.androidapp.data.preferences

interface UserPreferences {

    fun saveLoginCredentials(email: String, password: String, rememberMe: Boolean)
    fun saveUserInfo(userId: String, email: String)

    fun clearLoginCredentials()

    fun clearUserInfo()

    fun getEmail(): String?

    fun getPassword(): String?

    fun getRememberMe(): Boolean

    fun getUserId(): String?

    fun getUserEmail(): String?

    fun isLoggedIn(): Boolean

}
