package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.UserCache
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.model.UserState
import com.io.telegram.TelegramMessageHandler
import java.util.*

interface TelegramInteractor<Message, User> {

    suspend fun processingMessage(chatId: String, behavior: MessageInteractor.BehaviorForMessages): Message

    suspend fun processingUser(chatId: String, behavior: UserInteractor.BehaviorForUser) : User

    suspend fun getMessages(chatId: String, term: (MessageEntity) -> Boolean): List<MessageEntity>

    suspend fun getMessagesAsMapByMessageGroup(chatId: String, term: (MessageEntity) -> Boolean): Map<MessageGroup, List<Int>>

}

class TelegramInteractorImpl(
    private val messageInteractor: MessageInteractor<(suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity?)>,
    private val userInteractor: UserInteractor<(suspend () -> UserEntity?)>,
): TelegramInteractor<
        (suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity?),
        (suspend () -> UserEntity?)
   > {

    override suspend fun processingMessage(
        chatId: String,
        behavior: MessageInteractor.BehaviorForMessages
    ): suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity? {
        return when (behavior){
            is MessageInteractor.BehaviorForMessages.None -> { _ : Pair<Int, MessageGroup> -> null }
            is MessageInteractor.BehaviorForMessages.Delete -> { _ : Pair<Int, MessageGroup> -> null }
            is MessageInteractor.BehaviorForMessages.Save -> messageInteractor.saveMessage(chatId)
        }
    }

    override suspend fun processingUser(
        chatId: String,
        behavior: UserInteractor.BehaviorForUser
    ): suspend () -> UserEntity? {
        return when (behavior){
            is UserInteractor.BehaviorForUser.None -> { -> null }
            is UserInteractor.BehaviorForUser.Update -> userInteractor.updateUser(chatId, behavior.language, behavior.state)
            is UserInteractor.BehaviorForUser.GetOrCreate -> userInteractor.getOrSaveNewUser(chatId)
        }
    }

    override suspend fun getMessages(
        chatId: String,
        term: (MessageEntity) -> Boolean
    ): List<MessageEntity> {
        return messageInteractor.getMessage(chatId, term)
    }

    override suspend fun getMessagesAsMapByMessageGroup(
        chatId: String,
        term: (MessageEntity) -> Boolean
    ): Map<MessageGroup, List<Int>> {
        return getMessages(chatId, term).groupByTo(EnumMap(MessageGroup::class.java), {it.group}, {it.id})
    }

}