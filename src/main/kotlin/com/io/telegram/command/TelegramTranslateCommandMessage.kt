package com.io.telegram.command

import com.io.cache.entity.MessageEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.TypeMessage
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.editMessageText
import com.io.util.GetBooleanViaT
import com.io.util.GetListRViaFuncT

internal suspend inline fun editTranslateMessage(
    chatId: String,
    messageIds: GetListRViaFuncT<MessageEntity, TypeMessage>,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: GetBooleanViaT<MessageEntity> = { true }
    val newMessageIds = messageIds(filter)

    val result = mutableListOf<TelegramMessageHandler.Result>()

    var isFirst = true
    newMessageIds.forEach { typeMessage ->
        val message = TelegramMessageHandler.Result(
            chatId = chatId,
            behaviour = editMessageText(
                chat_id = chatId,
                text = typeMessage.get(language),
                messageId = typeMessage,
            ).asSendBehaviour(typeMessage.message.group.name),
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