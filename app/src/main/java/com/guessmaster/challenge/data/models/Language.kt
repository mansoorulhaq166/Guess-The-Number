package com.guessmaster.challenge.data.models

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val flagResId: Int? = null
)