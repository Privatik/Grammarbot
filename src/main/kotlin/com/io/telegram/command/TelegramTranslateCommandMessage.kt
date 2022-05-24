package com.io.telegram.command

import com.io.builder.InlineKeyBoardMarkupMachine
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.StartMessage
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.editMessageText
import com.io.util.GetMessageGroupToIntsViaFuncMessageEntity

internal suspend fun editTranslateMessage(
    chatId: String,
    messageIds: GetMessageGroupToIntsViaFuncMessageEntity,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: suspend (com.io.cache.entity.MessageEntity) -> Boolean = { true }
    val newMessageIds = messageIds(filter)

    val result = mutableListOf<TelegramMessageHandler.Result>()

    var isFirst = true
    newMessageIds.forEach { messageGroup, list ->
        val message = TelegramMessageHandler.Result(
            chatId = chatId,
            behaviour = editMessageText(
                chat_id = chatId,
                text = StartMessage.get(language),
                messageId = list.first(),
            ).asSendBehaviour(messageGroup.name),
            finishBehaviorUser = if (isFirst) UserInteractor.BehaviorForUser.Update(language = language)
                                else UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )

        if (isFirst){
            isFirst = false
        }

        result.add(message)
    }

    return result
}