package com.pth.androidapp.core.common

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import com.pth.androidapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "app_language_prefs"
        private const val KEY_SELECTED_LANGUAGE = "selected_language_choice"
    }

    data class LanguageItem(val code: String, val displayName: String)

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getAvailableLanguages(): List<LanguageItem> {
        return listOf(
            LanguageItem("en", context.getString(R.string.english)),
            LanguageItem("vi", context.getString(R.string.vietnamese)),
        )
    }

    fun getUserSelectedLanguageCode(): String {
        return sharedPrefs.getString(KEY_SELECTED_LANGUAGE, getSystemLanguage())!!
    }

    fun getAppLanguageCode(): String {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        return if (!appLocales.isEmpty) {
            appLocales[0]?.language ?: getUserSelectedLanguageCode()
        } else {
            getUserSelectedLanguageCode()
        }
    }

    fun getCurrentLanguagePosition(): Int {
        val currentCode = getAppLanguageCode()
        return getAvailableLanguages().indexOfFirst { it.code == currentCode }.coerceAtLeast(0)
    }

    fun setLanguage(languageCode: String) {
        // 1. Lưu lựa chọn của người dùng
        sharedPrefs.edit { putString(KEY_SELECTED_LANGUAGE, languageCode) }

        // 2. Yêu cầu hệ thống áp dụng ngôn ngữ mới
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    private fun getSystemLanguage(): String {
        val systemLocales = LocaleListCompat.getAdjustedDefault()
        return if (!systemLocales.isEmpty) {
            systemLocales[0]?.language ?: "en" // Fallback sang tiếng Anh nếu không xác định được
        } else {
            Locale.getDefault().language
        }
    }
}