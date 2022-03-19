package com.io.service

import com.io.resourse.translateKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

fun keyBoardMarkup(): InlineKeyboardMarkup {
    val translateButton = keyBoardMarkupTranslate()
    val markup = InlineKeyboardMarkup(
        listOf(listOf(translateButton))
    )
    return markup
}

fun keyBoardMarkupTranslate(): InlineKeyboardButton {
    val button = InlineKeyboardButton().apply {
        text = translateKeyboardMarkup.text
        callbackData = translateKeyboardMarkup.callbackData
    }
    return button
}