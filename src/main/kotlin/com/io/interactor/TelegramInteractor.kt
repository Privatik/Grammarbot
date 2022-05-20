package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.UserCache
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.UserState
import com.io.telegram.TelegramMessageHandler
import com.io.util.GetBooleanViaMessageEntity
import com.io.util.GetMessageEntityViaIntToMessageGroup
import com.io.util.GetMessageGroupToIntsViaFuncMessageEntity
import com.io.util.GetUserEntity
import java.util.*

interface TelegramInteractor<Message, User> {

    suspend fun processingMessage(chatId: String, behavior: MessageInteractor.BehaviorForMessages): Message

    suspend fun processingUser(chatId: String, behavior: UserInteractor.BehaviorForUser) : User

    suspend fun getMessages(chatId: String, term: GetBooleanViaMessageEntity): List<MessageEntity>

    suspend fun getMessagesAsMapByMessageGroup(chatId: String): GetMessageGroupToIntsViaFuncMessageEntity

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
            is MessageInteractor.BehaviorForMessages.None -> { _ : Pair<Int, MessageGroup> -> null }
            is MessageInteractor.BehaviorForMessages.Delete -> { _ : Pair<Int, MessageGroup> -> null }
            is MessageInteractor.BehaviorForMessages.Save -> messageInteractor.saveMessage(chatId)
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
        term: GetBooleanViaMessageEntity
    ): List<MessageEntity> {
        return messageInteractor.getMessage(chatId, term)
    }

    override suspend fun getMessagesAsMapByMessageGroup(
        chatId: String
    ): GetMessageGroupToIntsViaFuncMessageEntity {
        return { term ->
            getMessages(chatId, term).groupByTo(EnumMap(MessageGroup::class.java), {it.group}, {it.id})
        }
    }

}