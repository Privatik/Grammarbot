package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaMessageEntity
import com.io.util.GetMessageEntityViaIntToMessageGroup

interface MessageInteractor<T>{

    suspend fun saveMessage(chatId: String): T

    suspend fun deleteMessage(chaId: String): T

    suspend fun getMessage(chaId: String, term: suspend (MessageEntity) -> Boolean ): List<MessageEntity>

    sealed class BehaviorForMessages {
        object Save: BehaviorForMessages()
        object Delete: BehaviorForMessages()
        object None: BehaviorForMessages()
    }
}


class MessageInteractorImpl(
    private val messageCache: MessageCache,
): MessageInteractor<GetMessageEntityViaIntToMessageGroup> {

    override suspend fun saveMessage(chatId: String): GetMessageEntityViaIntToMessageGroup {
        return {
            messageCache.saveMessageId(chatId, it)
        }
    }

    override suspend fun deleteMessage(
        chaId: String
    ): GetMessageEntityViaIntToMessageGroup {
        return {
            messageCache.deleteMessageId(chaId, it.first)
        }
    }

    override suspend fun getMessage(
        chaId: String,
        term: GetBooleanViaMessageEntity
    ): List<MessageEntity> {
        return messageCache.getMessageIds(chaId, term)
    }

}

