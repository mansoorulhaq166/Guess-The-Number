package com.guessmaster.challenge.ui.screen.language

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guessmaster.challenge.data.models.Language
import com.guessmaster.challenge.utils.DataStoreManager
import com.guessmaster.challenge.utils.LocaleUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    languageProvider: LanguageProvider,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _languages = MutableStateFlow(languageProvider.getSupportedLanguages())
    val languages: StateFlow<List<Language>> = _languages

    private val _selectedLanguage = MutableStateFlow<Language?>(null)
    val selectedLanguage: StateFlow<Language?> = _selectedLanguage

    init {
        viewModelScope.launch {
            dataStoreManager.selectedLanguageCode.collect { code ->
                code?.let {
                    _selectedLanguage.value = _languages.value.find { it.code == code }
                }
            }
        }
    }

    fun onLanguageSelected(language: Language) {
        _selectedLanguage.value = language
    }

    fun applySelectedLanguageAndPersist(context: Context, onComplete: () -> Unit) {
        viewModelScope.launch {
            _selectedLanguage.value?.let { language ->
                // Save to preferences
                dataStoreManager.saveSelectedLanguageCode(language.code)

                // Apply locale change
                LocaleUtils.applyAppLocale(language.code, context)

                // Complete with activity restart
                onComplete()
            }
        }
    }
}