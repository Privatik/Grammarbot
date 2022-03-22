package com.io.util

import com.io.model.Language
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup

fun inlineKeyBoardMarkup(
    currentLanguage: Language,
    isTranslateButton: Boolean = false
): InlineKeyboardMarkup {
    val translateButton = keyBoardMarkupTranslate(currentLanguage)
    val markup = InlineKeyboardMarkup(
        listOf(listOf(translateButton))
    )
    return markup
}

private fun keyBoardMarkupTranslate(currentLanguage: Language): InlineKeyboardButton {
    val button = InlineKeyboardButton(
        text = translateKeyboardMarkup.text(currentLanguage),
        callback_data = translateKeyboardMarkup.callbackData
    )
    return button
}