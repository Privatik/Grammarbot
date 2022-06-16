package com.io.telegram.command

import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.resourse.Message
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.deleteMessage
import com.io.telegram.sendMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.extends.sectionMenuTerm
import com.io.util.getSectionMenuInlineKeyboardMarkup

suspend fun deleteLastMessage(
    userEntity: UserEntity,
    currentMessageId: Int,
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId

    val deleteMessage = TelegramMessageHandler.Result.Ordinary(
        chatId = chatId,
        behaviour = deleteMessage(
            chat_id = chatId,
            messageId = currentMessageId
        ).asDeleteBehaviour(MessageGroup.NONE.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    return listOf(deleteMessage)
}
