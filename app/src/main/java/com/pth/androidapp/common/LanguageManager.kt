package com.pth.androidapp.common

import android.content.Context
import android.content.res.Resources
import android.os.Build
import com.pth.androidapp.R
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale
import androidx.core.os.LocaleListCompat
import androidx.core.content.edit

class LanguageManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "app_language_prefs"
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
    }

    data class LanguageItem(val code: String, val displayName: String)

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getAvailableLanguages(): List<LanguageItem> {
        return listOf(
            LanguageItem("en", context.getString(R.string.english)),
            LanguageItem("vi", context.getString(R.string.vietnamese)),
        )
    }

    fun getCurrentLanguageCode(): String {
        return sharedPrefs.getString(KEY_SELECTED_LANGUAGE, getSystemLanguage())
            ?: getSystemLanguage()
    }

    fun getCurrentLanguagePosition(): Int {
        val currentCode = getCurrentLanguageCode()
        return getAvailableLanguages().indexOfFirst { it.code == currentCode }.takeIf { it >= 0 }
            ?: 0
    }

    fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)

        sharedPrefs.edit() { putString(KEY_SELECTED_LANGUAGE, languageCode) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            val localeList = LocaleListCompat.create(locale)
            AppCompatDelegate.setApplicationLocales(localeList)
        } else {
            // Android 7.0+
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)

            val config = context.resources.configuration
            config.setLocales(localeList)

            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }

    private fun getSystemLanguage(): String {
        return Resources.getSystem().configuration.locales[0].language
    }
}