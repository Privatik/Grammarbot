package com.io.telegram.command

import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.StartMessage
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.telegram.*
import com.io.util.extends.anotherLanguage
import com.io.util.inlineKeyBoardMarkup
import com.io.util.replyKeyBoardMarkup

internal fun sendStartMessage(
    chatId: String,
    messageIds: Map<String, List<Int>>,
    language: Language
): List<TelegramMessageHandler.Result>{
    val deleteMessages = mutableListOf<TelegramMessageHandler.Result>()
    messageIds.forEach { ( _, list ) ->
        deleteMessages.addAll(
            list.map { id ->
                TelegramMessageHandler.Result(
                    chatId = chatId,
                    behaviour =  deleteMessage(
                        chatId,
                        id
                    ).asDeleteBehaviour(MessageGroup.NONE.name),
                    finishBehaviorUser = TelegramMessageHandler.Result.BehaviorForUser.None,
                    finishBehaviorMessage = TelegramMessageHandler.Result.BehaviorForMessages.Delete
                )
            }
        )
    }

    val startMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour =  sendMessage(
            chat_id = chatId,
            text = StartMessage.get(language),
        ).asSendBehaviour(MessageGroup.START.name),
        finishBehaviorUser = TelegramMessageHandler.Result.BehaviorForUser.None,
        finishBehaviorMessage = TelegramMessageHandler.Result.BehaviorForMessages.Save
    )

    val choiceLessonMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = ChoiceLessonMessage.get(language),
            replyMarkup = inlineKeyBoardMarkup(
                language,
                isTranslateButton = true,
                isSectionButtons = true
            )
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = TelegramMessageHandler.Result.BehaviorForUser.None,
        finishBehaviorMessage = TelegramMessageHandler.Result.BehaviorForMessages.Save
    )

    return deleteMessages.apply {
        addAll(
            listOf(
                startMessage,
                choiceLessonMessage
            )
        )
    }
}

internal fun editStartMessage(
    chatId: String,
    messageIds: Map<String, List<Int>>,
    language: Language
): List<TelegramMessageHandler.Result>{
    val startMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = editMessageText(
            chat_id = chatId,
            text = StartMessage.get(language),
            messageId = messageIds[MessageGroup.START.name]!!.first(),
        ).asSendBehaviour(MessageGroup.START.name),
        finishBehaviorUser = TelegramMessageHandler.Result.BehaviorForUser.Update(language = language),
        finishBehaviorMessage = TelegramMessageHandler.Result.BehaviorForMessages.Save
    )

    val choiceLessonMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = editMessageText(
            chat_id = chatId,
            text = ChoiceLessonMessage.get(language),
            messageId = messageIds[MessageGroup.CHOICE_SECTION.name]!!.first(),
            replyMarkup = inlineKeyBoardMarkup(
                language,
                isTranslateButton = true,
                isSectionButtons = true
            )
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = TelegramMessageHandler.Result.BehaviorForUser.Update(language = language),
        finishBehaviorMessage = TelegramMessageHandler.Result.BehaviorForMessages.Save
    )

    return listOf(
        startMessage,
        choiceLessonMessage
    )
}