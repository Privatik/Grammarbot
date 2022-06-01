package com.io.telegram.command

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.cache.entity.Entity
import com.io.cache.entity.MessageEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.resourse.ChoiceLessonMessage
import com.io.telegram.TelegramMessageHandler
import com.io.telegram.sendMessage
import com.io.util.GetBooleanViaT
import com.io.util.GetListRViaFuncT
import com.io.util.extends.createMessage
import com.io.util.extends.messageTerm
import com.io.util.extends.sectionTerm

suspend fun sendSectionMessage(
    chatId: String,
    sectionId: String,
    messageIds: GetListRViaFuncT<Entity.SectionRuleEntity, TypeMessage>,
    language: Language
): List<TelegramMessageHandler.Result>{

    val filter: GetBooleanViaT<Entity.SectionRuleEntity> = sectionTerm {
        it.id == sectionId
    }

    val message = messageIds(filter).first() as TypeMessage.Section

    val sectionMessage = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = message.section.createMessage().get(language),
            replyMarkup = InlineKeyBoardMarkupBuilder()
                .addTranslateButton()
                .build()
        ).asSendBehaviour(MessageGroup.SECTION.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.Update(state = UserState.PRE_LEARN),
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.SaveAsSection(sectionId)
    )

    return listOf(sectionMessage)

}