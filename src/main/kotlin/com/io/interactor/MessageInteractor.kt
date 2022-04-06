package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup

interface MessageInteractor<T>{

    suspend fun saveMessage(chatId: String): T

    suspend fun deleteMessage(chaId: String, message: Int): T

    suspend fun getMessage(chaId: String, term: (MessageEntity) -> Boolean ): List<MessageEntity>

    sealed class BehaviorForMessages {
        object Save: BehaviorForMessages()
        object Delete: BehaviorForMessages()
        object None: BehaviorForMessages()
    }
}


class MessageInteractorImpl(
    private val messageCache: MessageCache,
): MessageInteractor<(suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity?)> {

    override suspend fun saveMessage(chatId: String): (suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity?) {
        return {
            messageCache.saveMessageId(chatId, it)
        }
    }

    override suspend fun deleteMessage(
        chaId: String,
        message: Int
    ): suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity? {
        return {
            messageCache.deleteMessageId(chaId, message)
        }
    }

    override suspend fun getMessage(
        chaId: String,
        term: (MessageEntity) -> Boolean
    ): List<MessageEntity> {
        return messageCache.getMessageIds(chaId, term)
    }

}

