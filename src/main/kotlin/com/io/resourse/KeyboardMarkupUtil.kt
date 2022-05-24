package com.io.resourse

import com.io.model.Language

data class KeyboardMarkup(
    val text: (Language) -> String,
    val callbackData: String?
)

fun createKeyboardMarkup(message: Message, callbackData: String): KeyboardMarkup{
    return KeyboardMarkup(
        text = message::get,
        callbackData = callbackData
    )
}

val translateKeyboardMarkup = KeyboardMarkup(
    text = TranslateMessage::get,
    callbackData = "translate"
)
