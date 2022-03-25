package com.io.telegram.command

import com.io.ChoiceLessonMessage
import com.io.StartMessage
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.telegram.TelegramBehaviour
import com.io.telegram.editMessageText
import com.io.telegram.sendMessage
import com.io.util.inlineKeyBoardMarkup

fun sendStartMessage(
    chatId: String,
    language: Language
): List<TelegramBehaviour>{
    val startMessage = sendMessage(
        chat_id = chatId,
        text = StartMessage.get(language),
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isTranslateButton = true
        )
    )

    val choiceLessonMessage = sendMessage(
        chat_id = chatId,
        text = ChoiceLessonMessage.get(language),
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isSectionButtons = true
        )
    )

    return listOf(
        startMessage.asSendBehaviour(MessageGroup.START::class.java.name),
        choiceLessonMessage.asSendBehaviour(MessageGroup.CHOICE_SECTION::class.java.name,2000)
    )
}

fun editStartMessage(
    chatId: String,
    messageIds: Map<String, Int>,
    language: Language
): List<TelegramBehaviour>{
    val startMessage = editMessageText(
        chat_id = chatId,
        text = StartMessage.get(language),
        messageId = messageIds[MessageGroup.START::class.java.name]!!,
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isTranslateButton = true
        )
    )

    val choiceLessonMessage = editMessageText(
        chat_id = chatId,
        text = ChoiceLessonMessage.get(language),
        messageId = messageIds[MessageGroup.CHOICE_SECTION::class.java.name]!!,
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isSectionButtons = true
        )
    )

    return listOf(
        startMessage.asSendBehaviour(MessageGroup.START::class.java.name),
        choiceLessonMessage.asSendBehaviour(MessageGroup.CHOICE_SECTION::class.java.name)
    )
}