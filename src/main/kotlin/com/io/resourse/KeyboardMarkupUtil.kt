package com.io.resourse

data class KeyboardMarkup(
    val text: String,
    val callbackData: String
)

val translateKeyboardMarkup = KeyboardMarkup(
    text = "Перевести",
    callbackData = "translate"
)
