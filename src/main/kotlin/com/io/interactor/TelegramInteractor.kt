package com.io.interactor

import com.io.cache.entity.MessageEntity
import com.io.model.TypeMessage
import com.io.util.*
import java.util.*

interface TelegramInteractor<Message, User> {

    suspend fun processingMessage(chatId: String, behavior: MessageInteractor.BehaviorForMessages): Message

    suspend fun processingUser(chatId: String, behavior: UserInteractor.BehaviorForUser) : User

    suspend fun getMessages(chatId: String): GetListRViaFuncT<MessageEntity, TypeMessage>

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
            is MessageInteractor.BehaviorForMessages.Delete -> messageInteractor.deleteMessages(chatId)
            is MessageInteractor.BehaviorForMessages.Save -> messageInteractor.saveMessage(chatId)
            is MessageInteractor.BehaviorForMessages.SaveAsSection -> messageInteractor.saveAsSectionMessage(chatId, behavior.sectionId)
            is MessageInteractor.BehaviorForMessages.SaveAsTask -> messageInteractor.saveAsLearnMessage(chatId, behavior.taskId, behavior.learnState)
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

    override suspend fun getMessages(chatId: String): GetListRViaFuncT<MessageEntity, TypeMessage> {
        return {
            messageInteractor.getMessages(chatId, it)
        }
    }

}