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
import com.io.util.getSectionMenuInlineKeyboardMarkup

suspend fun sendStartMessage(
    userEntity: UserEntity,
    currentMessageId: Int,
    messageIds: GetListRViaFuncT<com.io.cache.entity.Entity, TypeMessage>,
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId
    val language = userEntity.currentLanguage

    val filter = messageTermWithCheckChatId(chatId){
        it.group == MessageGroup.START || it.group == MessageGroup.CHOICE_SECTION
    }

    val messages = messageIds(filter)

    if (messages.size > 1){
        val deleteMessage = TelegramMessageHandler.Result(
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

    val sectionMenu = messages.first() as TypeMessage.SectionMenu

    val startMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour =  sendMessage(
            chat_id = chatId,
            text = Message.StartMessage.get(language),
        ).asSendBehaviour(MessageGroup.START.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
    )

    val choiceLessonMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = Message.ChoiceLessonMessage.get(language),
            replyMarkup = getSectionMenuInlineKeyboardMarkup(language, sectionMenu.sections)
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
    )

    return listOf(
        startMessage,
        choiceLessonMessage
    )
}
