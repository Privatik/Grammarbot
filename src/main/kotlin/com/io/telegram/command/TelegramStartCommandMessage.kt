package com.io.telegram.command

import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.StartMessage
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.telegram.*
import com.io.util.inlineKeyBoardMarkup
import com.io.util.replyKeyBoardMarkup

fun sendStartMessage(
    chatId: String,
    messageIds: Map<String, List<Int>>,
    language: Language
): List<TelegramBehaviour>{
    val deleteMessages = mutableListOf<TelegramBehaviour>()
    messageIds.forEach { ( _, list ) ->
        list.forEach { id ->
            deleteMessages.add(
                deleteMessage(
                    chatId,
                    id
                ).asDeleteBehaviour(MessageGroup.NONE.name)
            )
        }
    }

    val startMessage = sendMessage(
        chat_id = chatId,
        text = StartMessage.get(language),
    )

    val choiceLessonMessage = sendMessage(
        chat_id = chatId,
        text = ChoiceLessonMessage.get(language),
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isTranslateButton = true,
            isSectionButtons = true
        )
    )

    return deleteMessages.apply {
        addAll(
            listOf(
                startMessage.asSendBehaviour(MessageGroup.START.name),
                choiceLessonMessage.asSendBehaviour(MessageGroup.CHOICE_SECTION.name)
            )
        )
    }
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
    )

    val choiceLessonMessage = editMessageText(
        chat_id = chatId,
        text = ChoiceLessonMessage.get(language),
        messageId = messageIds[MessageGroup.CHOICE_SECTION.name]!!.first(),
        replyMarkup = inlineKeyBoardMarkup(
            language,
            isTranslateButton = true,
            isSectionButtons = true
        )
    )

    return listOf(
        startMessage.asSendBehaviour(MessageGroup.START.name),
        choiceLessonMessage.asSendBehaviour(MessageGroup.CHOICE_SECTION.name)
    )
}