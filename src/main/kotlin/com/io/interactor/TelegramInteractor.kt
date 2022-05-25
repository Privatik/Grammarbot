package com.io.interactor

import com.io.cache.entity.MessageEntity
import com.io.model.TypeMessage
import com.io.util.*
import java.util.*

interface TelegramInteractor<Message, User> {

    suspend fun processingMessage(chatId: String, behavior: MessageInteractor.BehaviorForMessages): Message

    suspend fun processingUser(chatId: String, behavior: UserInteractor.BehaviorForUser) : User

    suspend fun getMessages(chatId: String): GetListMessageEntityViaFuncMessageEntity

}

class TelegramInteractorImpl(
    private val messageInteractor: MessageInteractor<GetMessageEntityViaIntToMessageGroup>,
    private val userInteractor: UserInteractor<GetUserEntity>,
): TelegramInteractor<GetMessageEntityViaIntToMessageGroup, GetUserEntity> {

    override suspend fun processingMessage(
        chatId: String,
        behavior: MessageInteractor.BehaviorForMessages
    ): GetMessageEntityViaIntToMessageGroup {
        return when (behavior){
            is MessageInteractor.BehaviorForMessages.None -> { _ , _ -> null }
            is MessageInteractor.BehaviorForMessages.Delete -> messageInteractor.deleteMessage(chatId)
            is MessageInteractor.BehaviorForMessages.Save<*> -> messageInteractor.saveMessage(chatId)
        }
    }

    override suspend fun processingUser(
        chatId: String,
        behavior: UserInteractor.BehaviorForUser
    ): GetUserEntity {
        return when (behavior){
            is UserInteractor.BehaviorForUser.None -> { -> null }
            is UserInteractor.BehaviorForUser.Update -> userInteractor.updateUser(chatId, behavior.language, behavior.state)
            is UserInteractor.BehaviorForUser.GetOrCreate -> userInteractor.getOrSaveNewUser(chatId)
        }
    }

    override suspend fun getMessages(
        chatId: String,
    ): GetListRViaFuncT<MessageEntity, TypeMessage> {
        return {
            messageInteractor.getMessage(chatId, it)
        }
    }

}