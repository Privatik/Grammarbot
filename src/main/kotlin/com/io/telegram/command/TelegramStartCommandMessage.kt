package com.io.telegram.command

import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.StartMessage
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.telegram.TelegramBehaviour
import com.io.telegram.editMessageText
import com.io.telegram.sendMessage
import com.io.util.inlineKeyBoardMarkup
import com.io.util.replyKeyBoardMarkup

fun sendStartMessage(
    chatId: String,
    language: Language
): List<TelegramBehaviour>{
    val startMessage = sendMessage(
        chat_id = chatId,
        text = StartMessage.get(language),
    )

    val choiceLessonMessage = sendMessage(
        chat_id = chatId,
        text = ChoiceLessonMessage.get(language),
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isSectionButtons = true
        )
    )

    val updateStartMessage = editMessageText(
        chat_id = chatId,
        text = StartMessage.get(language),
        messageId = 0,
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isTranslateButton = true
        )
    )

    return listOf(
        startMessage.asUpdateBehaviour(MessageGroup.START.name, updateStartMessage, 0, 2000),
        choiceLessonMessage.asSendBehaviour(MessageGroup.CHOICE_SECTION.name)
    )
}

fun editStartMessage(
    chatId: String,
    messageIds: Map<String, List<Int>>,
    language: Language
): List<TelegramBehaviour>{
    val startMessage = editMessageText(
        chat_id = chatId,
        text = StartMessage.get(language),
        messageId = messageIds[MessageGroup.START.name]!!.first(),
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isTranslateButton = true
        )
    )

    val choiceLessonMessage = editMessageText(
        chat_id = chatId,
        text = ChoiceLessonMessage.get(language),
        messageId = messageIds[MessageGroup.CHOICE_SECTION.name]!!.first(),
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isSectionButtons = true
        )
    )

    return listOf(
        startMessage.asSendBehaviour(MessageGroup.START.name),
        choiceLessonMessage.asSendBehaviour(MessageGroup.CHOICE_SECTION.name)
    )
}