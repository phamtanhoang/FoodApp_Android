package com.pth.androidapp.common

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LanguageUtils {
    fun setAppLanguage(context: Context, languageCode: String): Context {
        saveLanguage(context, languageCode)

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    fun getAppLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getString("language", getDeviceLanguage()) ?: getDeviceLanguage()
    }

    private fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("language", languageCode).apply()
    }

    private fun getDeviceLanguage(): String {
        val deviceLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.getDefault(Locale.Category.DISPLAY)
        } else {
            Locale.getDefault()
        }

        val deviceLanguage = deviceLocale.language

        // If device language is in our supported languages, use it
        return if (Constants.languages.values.contains(deviceLanguage)) {
            deviceLanguage
        } else {
            // Otherwise default to English
            "en"
        }
    }
}