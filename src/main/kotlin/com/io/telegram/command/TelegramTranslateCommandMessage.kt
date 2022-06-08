package com.io.telegram.command

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.model.UserState
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
        TelegramMessageHandler.Result(
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
            UserState.LEARN -> { it.group == MessageGroup.LEARN ||  it.group == MessageGroup.SECTION}
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

    val supportMessageWithFinishButton = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = sendMessage(
            chat_id = chatId,
            text = "⁠",
            replyMarkup = getTaskReplyKeyboardMarkup(language)
        ).asSendBehaviour(MessageGroup.NONE.name),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

    val deleteSupportMessageWithFinishButton: CreateTelegramMessageHandlerResult = {
        TelegramMessageHandler.Result(
            chatId = chatId,
            behaviour = deleteMessage(
                chat_id = chatId,
                messageId = it,
            ).asDeleteBehaviour(MessageGroup.NONE.name),
            finishBehaviorUser = UserInteractor.BehaviorForUser.None,
            finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
        )
    }

    val l = TelegramMessageHandler.Result(
        chatId = chatId,
        behaviour = CreateTelegramBehaviour{

        }(),
        finishBehaviorUser = UserInteractor.BehaviorForUser.None,
        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
    )

//    return TelegramMessageHandler.Result(
//        chatId = chatId,
//        behaviour = (supportMessageWithFinishButton to listOf(deleteSupportMessageWithFinishButton)),
//        finishBehaviorUser = if (index == 0) UserInteractor.BehaviorForUser.Update(language = language)
//        else UserInteractor.BehaviorForUser.None,
//        finishBehaviorMessage = MessageInteractor.BehaviorForMessages.None
//    )
    return emptyList()
}