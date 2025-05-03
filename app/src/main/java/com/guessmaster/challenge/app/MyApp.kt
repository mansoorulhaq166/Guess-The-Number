package com.guessmaster.challenge.app

import android.app.Application
import android.content.Context
import com.guessmaster.challenge.utils.DataStoreManager
import com.guessmaster.challenge.utils.LocaleUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApp : Application() {
    private val scope = CoroutineScope(SupervisorJob())
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate() {
        super.onCreate()
        dataStoreManager = DataStoreManager.getInstance(this)
        setupLanguage()
    }

    private fun setupLanguage() {
        scope.launch {
            dataStoreManager.selectedLanguageCode.collect { langCode ->
                langCode?.let { LocaleUtils.applyAppLocale(it, this@MyApp) }
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtils.wrapContext(base, "en"))
    }

    override fun onTerminate() {
        super.onTerminate()
        scope.cancel()
    }
}