package com.io.service

import com.io.StartMessage
import com.io.model.Language
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup
import com.io.telegram.KeyboardButton
import com.io.telegram.ReplyKeyboardMarkup

fun replyKeyBoardMarkup(
    currentLanguage: Language
): ReplyKeyboardMarkup {
    val translateButton = keyBoardMarkupStartLearning(currentLanguage)
    val markup = ReplyKeyboardMarkup(
        keyboard = listOf(listOf(translateButton)),
        resize_keyboard = true
    )
    return markup
}

private fun keyBoardMarkupStartLearning(currentLanguage: Language): KeyboardButton{
    val button = KeyboardButton(
        text = translateKeyboardMarkup.text(currentLanguage)
    )
    return button
}