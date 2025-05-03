package com.guessmaster.challenge.ui.screen.language

import com.guessmaster.challenge.data.models.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageProvider @Inject constructor() {
    fun getSupportedLanguages(): List<Language> = listOf(
        Language("en", "English", "English"),
        Language("es", "Spanish", "Español"),
        Language("fr", "French", "Français"),
        Language("de", "German", "Deutsch"),
        Language("it", "Italian", "Italiano"),
        Language("ja", "Japanese", "日本語"),
        Language("ko", "Korean", "한국어"),
        Language("zh", "Chinese (Simplified)", "中文"),
        Language("zh-TW", "Chinese (Traditional)", "繁體中文"),
        Language("ru", "Russian", "Русский"),
        Language("ar", "Arabic", "العربية"),
        Language("hi", "Hindi", "हिन्दी"),
        Language("pt", "Portuguese", "Português"),
        Language("ur", "Urdu", "اردو"),
        Language("bn", "Bengali", "বাংলা"),
        Language("tr", "Turkish", "Türkçe"),
        Language("id", "Indonesian", "Bahasa Indonesia"),
        Language("vi", "Vietnamese", "Tiếng Việt"),
        Language("tl", "Filipino", "Filipino"),
        Language("th", "Thai", "ไทย"),
        Language("fa", "Persian", "فارسی"),
        Language("ms", "Malay", "Bahasa Melayu"),
        Language("pl", "Polish", "Polski"),
        Language("uk", "Ukrainian", "Українська"),
        Language("ro", "Romanian", "Română"),
        Language("nl", "Dutch", "Nederlands"),
        Language("el", "Greek", "Ελληνικά"),
        Language("he", "Hebrew", "עברית"),
        Language("sw", "Swahili", "Kiswahili"),
        Language("ta", "Tamil", "தமிழ்")
    )
}