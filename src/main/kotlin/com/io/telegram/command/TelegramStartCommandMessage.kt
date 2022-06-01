package com.io.telegram.command

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.ChoiceSectionId
import com.io.resourse.StartMessage
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.deleteMessage
import com.io.telegram.sendMessage
import com.io.util.GetBooleanViaT
import com.io.util.GetListRViaFuncT
import com.io.util.extends.messageTerm
import com.io.util.extends.messageTermWithCheckChatId

suspend fun sendStartMessage(
    chatId: String,
    currentMessageId: Int,
    messageIds: GetListRViaFuncT<com.io.cache.entity.Entity.MessageEntity, TypeMessage>,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: GetBooleanViaT<com.io.cache.entity.Entity.MessageEntity> = messageTermWithCheckChatId(chatId){
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
            replyMarkup = InlineKeyBoardMarkupBuilder(language)
                .addTranslateButton()
                .addSectionButton(sectionMenu.sections)
                .build()
        ).asSendBehaviour(MessageGroup.CHOICE_SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.Save
    )

    return listOf(
        startMessage,
        choiceLessonMessage
    )
}
