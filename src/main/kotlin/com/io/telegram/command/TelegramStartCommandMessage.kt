package com.io.telegram.command

import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.StartMessage
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.telegram.*
import com.io.util.GetBooleanViaMessageEntity
import com.io.util.GetMessageGroupToIntsViaFuncMessageEntity
import com.io.util.extends.anotherLanguage
import com.io.util.inlineKeyBoardMarkup
import com.io.util.replyKeyBoardMarkup

internal suspend fun sendStartMessage(
    chatId: String,
    messageIds: GetMessageGroupToIntsViaFuncMessageEntity,
    language: Language
): List<TelegramMessageHandler.Result>{
    val deleteMessages = mutableListOf<TelegramMessageHandler.Result>()

    val filter: GetBooleanViaMessageEntity = {
        it.group == MessageGroup.START || it.group == MessageGroup.CHOICE_SECTION
    }

    messageIds(filter)
        .forEach { ( _, list ) ->
            deleteMessages.addAll(
                list.map { id ->
                    TelegramMessageHandler.Result(
                        chatId = chatId,
                        behaviour =  deleteMessage(
                            chatId,
                            id
                        ).asDeleteBehaviour(MessageGroup.NONE.name),
                        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
                        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Delete
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
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
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
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
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

internal suspend fun editStartMessage(
    chatId: String,
    messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: suspend (com.io.cache.entity.MessageEntity) -> Boolean = {
        it.group == MessageGroup.START || it.group == MessageGroup.CHOICE_SECTION
    }
    val newMessageIds = messageIds(filter)

    val startMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = editMessageText(
            chat_id = chatId,
            text = StartMessage.get(language),
            messageId = newMessageIds[MessageGroup.START]!!.first(),
        ).asSendBehaviour(MessageGroup.START.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(language = language),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    val choiceLessonMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = editMessageText(
            chat_id = chatId,
            text = ChoiceLessonMessage.get(language),
            messageId = newMessageIds[MessageGroup.CHOICE_SECTION]!!.first(),
            replyMarkup = inlineKeyBoardMarkup(
                language,
                isTranslateButton = true,
                isSectionButtons = true
            )
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    return listOf(
        startMessage,
        choiceLessonMessage
    )
}