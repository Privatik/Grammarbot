package com.io.util

import com.io.model.Language
import com.io.resourse.startLearningKeyboardMarkup
import com.io.telegram.KeyboardButton
import com.io.telegram.ReplyKeyboardMarkup

fun replyKeyBoardMarkup(
    currentLanguage: Language,
    isStartLearning: Boolean = false
): ReplyKeyboardMarkup {
    val translateButton = keyBoardMarkupStartLearning(currentLanguage)
    val markup = ReplyKeyboardMarkup(
        keyboard = listOf(listOf(translateButton)),
        resize_keyboard = true,
    )
    return markup
}

private fun keyBoardMarkupStartLearning(currentLanguage: Language): KeyboardButton{
    val button = KeyboardButton(
        text = startLearningKeyboardMarkup.text(currentLanguage)
    )
    return button
}