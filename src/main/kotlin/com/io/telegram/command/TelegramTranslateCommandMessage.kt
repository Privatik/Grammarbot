package com.io.telegram.command

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.*
import com.io.resourse.Message.Companion.TranslateMessage
import com.io.telegram.*
import com.io.util.*
import com.io.util.extends.messageTermWithCheckChatId

suspend fun editTranslateMessage(
    userEntity: UserEntity,
    messageIds: GetListRViaFuncT<Entity, TypeMessage>,
    language: Language
): List<TelegramMessageHandler.Result>{

    val chatId = userEntity.chatId

    val filterByUser = filterRuleByUserState(userEntity.currentState)
    val filter = messageTermWithCheckChatId(chatId, filterByUser)
    val newMessageIds = messageIds(filter)

    val supportMessages = if (userEntity.currentState == UserState.LEARN){
        createFinishMessages(userEntity, language)
    } else listOf<TelegramMessageHandler.Result>()

    return newMessageIds.mapIndexed { index, typeMessage ->
        TelegramMessageHandler.Result.Ordinary(
            chatId = chatId,
            behaviour = editMessageText(
                chat_id = chatId,
                text = typeMessage.getMessage().get(language),
                messageId = typeMessage.message.id,
                replyMarkup = typeMessage.getReplyKeyboard(userEntity.currentState, language)
            ).asSendBehaviour(typeMessage.message.group.name),
            finishBehaviorUser = if (index == 0) UserInteractor.BehaviorForUser.Update(language = language)
            else UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )
    } + supportMessages
}

private fun filterRuleByUserState(state: UserState): GetBooleanViaT<Entity.MessageEntity> {
    return {
        when (state) {
            UserState.RELAX,
            UserState.PRE_LEARN -> {
                it.group == MessageGroup.START ||
                it.group == MessageGroup.CHOICE_SECTION ||
                it.group == MessageGroup.SECTION
            }
            UserState.LEARN -> { it.group == MessageGroup.ANSWER_ON_TASK ||  it.group == MessageGroup.SECTION}
            UserState.POST_LEARN,
            UserState.DESCRIBED_ERROR -> throw NoSuchMethodError()
        }
    }
}

private fun createFinishMessages(
    userEntity: UserEntity,
    language: Language
) : List<TelegramMessageHandler.Result>{
    val chatId = userEntity.chatId

    val supportMessageWithFinishButton = TelegramMessageHandler.Result.Ordinary(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = TranslateMessage.get(language),
            replyMarkup = getTaskReplyKeyboardMarkup(language)
        ).asSendBehaviour(MessageGroup.NONE.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    val deleteMessage = MultiBehaviours(
        behaviour = { id: Int ->
            deleteMessage(
                chat_id = chatId,
                messageId = id,
            ).asDeleteBehaviour(MessageGroup.NONE.name)
        },
        name = MessageGroup.NONE.name,
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    val order = TelegramMessageHandler.Result.Order(
        behaviour = supportMessageWithFinishButton,
        behaviours = listOf(deleteMessage)
    )

    return listOf(order)
}