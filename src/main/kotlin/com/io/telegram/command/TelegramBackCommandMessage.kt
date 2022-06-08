package com.io.telegram.command

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.resourse.Message
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.deleteMessage
import com.io.telegram.editMessageText
import com.io.telegram.sendMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.messageTermWithCheckChatId
import com.io.util.getMessage
import com.io.util.getReplyKeyboard
import com.io.util.getSectionMenuInlineKeyboardMarkup

suspend fun stepBack(
    userEntity: UserEntity,
    messageIds: GetListRViaFuncT<Entity, TypeMessage>
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId
    val language = userEntity.currentLanguage

    val filterSection = messageTermWithCheckChatId(chatId){ MessageGroup.SECTION == it.group  }
    val filterSectionMenu = messageTermWithCheckChatId(chatId){ MessageGroup.CHOICE_SECTION == it.group  }
    val sectionMessage = messageIds(filterSection).first()
    val sectionMenuMessage = messageIds(filterSectionMenu).first() as TypeMessage.SectionMenu

    val startMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = deleteMessage(
            chat_id = chatId,
            messageId = sectionMessage.message.id,
        ).asDeleteBehaviour(MessageGroup.NONE.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Delete
    )

    val choiceLessonMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = Message.ChoiceLessonMessage.get(language),
            replyMarkup = getSectionMenuInlineKeyboardMarkup(language, sectionMenuMessage.sections)
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(state = UserState.RELAX),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
    )

    return listOf(startMessage, choiceLessonMessage)
}