package com.io.telegram

import com.io.KnowWhenStartMessage
import com.io.StartMessage
import com.io.model.Language
import com.io.util.inlineKeyBoardMarkup
import com.io.util.replyKeyBoardMarkup

fun getStartMessages(message: Message, language: Language): List<TelegramBehaviour> {
    val list = mutableListOf<TelegramBehaviour>()

    val startTextMessage = StartMessage.get(language)
    val knowWhenStartTextMessage = KnowWhenStartMessage.get(language)

    val knowWhenStartSendMessage = sendMessage(
        chat_id = message.chat.id,
        text = knowWhenStartTextMessage,
        replyMarkup = replyKeyBoardMarkup(
            currentLanguage = language,
            isStartLearning = true
        )
    )

    val startSendMessage = sendMessage(
        chat_id = message.chat.id,
        text = startTextMessage,
        replyMarkup = inlineKeyBoardMarkup(
            currentLanguage = language,
            isTranslateButton = true
        )
    )

    list.add(startSendMessage.asSendBehaviour())
    list.add(knowWhenStartSendMessage.asSendBehaviour(2000))

    return list
}