package com.io.util

import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.InlineKeyboardButton
import com.io.telegram.InlineKeyboardMarkup

fun keyBoardMarkup(): InlineKeyboardMarkup {
    val translateButton = keyBoardMarkupTranslate()
    val markup = InlineKeyboardMarkup(
        listOf(listOf(translateButton))
    )
    return markup
}

fun keyBoardMarkupTranslate(): InlineKeyboardButton {
    val button = InlineKeyboardButton(
        text = translateKeyboardMarkup.text,
        callback_data = translateKeyboardMarkup.callbackData
    )
    return button
}