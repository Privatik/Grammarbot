package com.io.telegram.command

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.deleteMessage
import com.io.telegram.sendMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.createMessage
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.extends.sectionTerm
import com.io.util.getSectionInlineKeyboardMarkup

suspend fun sendSectionMessage(
    userEntity: UserEntity,
    sectionId: String,
    messageIds: GetListRViaFuncT<Entity, TypeMessage>
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId
    val language = userEntity.currentLanguage

    val filterChoiceMessage = messageTermWithCheckChatId(
        chatId,
        MessageGroup.CHOICE_SECTION
    ){
        it.group == MessageGroup.CHOICE_SECTION
    }

    val filterSection = sectionTerm {
        it.id == sectionId
    }

    val message = messageIds(filterChoiceMessage).first()
    val messageSection = messageIds(filterSection).first() as TypeMessage.Section

    val choiceMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = deleteMessage(
            chat_id = chatId,
            messageId = message.message.id,
        ).asDeleteBehaviour(MessageGroup.NONE.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(state = UserState.PRE_LEARN),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Delete
    )

    val sectionMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = messageSection.section.createMessage().get(language),
            replyMarkup = getSectionInlineKeyboardMarkup(language)
        ).asSendBehaviour(MessageGroup.SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.SaveAsSection(sectionId)
    )

    return listOf(choiceMessage,sectionMessage)

}