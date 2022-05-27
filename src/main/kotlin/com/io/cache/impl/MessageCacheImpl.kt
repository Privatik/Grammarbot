package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT

class MessageCacheImpl: MessageCache {
    private val messages = mutableListOf<MessageEntity>()

    private fun print(){
        println("Message $messages")
    }


    override suspend fun saveMessageId(chatId: String, messageId: Long, messageGroup: MessageGroup): MessageEntity {
        val entity = MessageEntity(
            id = messageId,
            chatId = chatId,
            group = messageGroup
        )
        messages.add(entity)
        print()
        return entity
    }

    override suspend fun deleteMessage(chatId: String, messageId: Long): MessageEntity? {
        val removeMessageEntity = messages.find { it.chatId == chatId && messageId == it.id  }
        messages.remove(removeMessageEntity)
        return removeMessageEntity
    }


    override suspend fun getMessages(chatId: String, term: GetBooleanViaT<MessageEntity>): List<MessageEntity> {
        return messages.filter{ term(it) && it.chatId == chatId }
    }

}