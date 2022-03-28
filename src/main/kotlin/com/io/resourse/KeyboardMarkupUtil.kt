package com.io.resourse

import com.io.model.Language

data class KeyboardMarkup(
    val text: (Language) -> String,
    val callbackData: String?
)

val translateKeyboardMarkup = KeyboardMarkup(
    text = TranslateMessage::get,
    callbackData = "translate"
)

val startLearningKeyboardMarkup = KeyboardMarkup(
    text = ChoiceLessonMessage::get,
    callbackData = null
)
