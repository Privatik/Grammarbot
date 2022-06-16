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

//    val supportMessages = if (userEntity.currentState == UserState.LEARN){
//        createFinishMessages(userEntity, language)
//    } else emptyList()

    val lastMessage = newMessageIds.maxByOrNull { it.message.time }!!

    return newMessageIds.mapIndexed { index, typeMessage ->
        val isLastMessage = typeMessage.message.id == lastMessage.message.id
        TelegramMessageHandler.Result.Ordinary(
            chatId = chatId,
            behaviour = editMessageText(
                chat_id = chatId,
                text = typeMessage.getMessage().get(language),
                messageId = typeMessage.message.id,
                replyMarkup = typeMessage.getReplyKeyboard(userEntity.currentState, language, isLastMessage)
            ).asSendBehaviour(typeMessage.message.group.name),
            finishBehaviorUser = if (index == 0) UserInteractor.BehaviorForUser.Update(language = language)
            else UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )
    }
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
            UserState.PAUSE_LEARN,
            UserState.LEARN -> { it.group == MessageGroup.RIGHT_ANSWER_ON_TASK ||  it.group == MessageGroup.SECTION}
            UserState.POST_LEARN,
            UserState.DESCRIBED_ERROR -> throw NoSuchMethodError()
        }
    }
}