package com.guessmaster.challenge.utils

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

object LocaleUtils {
    fun applyAppLocale(languageCode: String, context: Context): Context {
        return try {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            context.createConfigurationContext(config)
        } catch (e: Exception) {
            Log.e("LocaleUtils", "Error changing locale", e)
            context
        }
    }

    fun wrapContext(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}