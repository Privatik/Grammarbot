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
import com.io.util.extends.messageTerm
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.getMessage
import com.io.util.getReplyKeyboard

suspend fun editTranslateMessage(
    chatId: String,
    messageIds: GetListRViaFuncT<MessageEntity, TypeMessage>,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: GetBooleanViaT<MessageEntity> = messageTermWithCheckChatId(chatId){ true }
    val newMessageIds = messageIds(filter)

    return newMessageIds.mapIndexed { index, typeMessage ->
        TelegramMessageHandler.Result(
            chatId = chatId,
            behaviour = editMessageText(
                chat_id = chatId,
                text = typeMessage.getMessage().get(language),
                messageId = typeMessage.message.id,
                replyMarkup = typeMessage.getReplyKeyboard(language)
            ).asSendBehaviour(typeMessage.message.group.name),
            finishBehaviorUser = if (index == 0) UserInteractor.BehaviorForUser.Update(language = language)
            else UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )
    }
}