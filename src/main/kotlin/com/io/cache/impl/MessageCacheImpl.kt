package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup
import com.io.model.asMessageGroup
import java.util.*

class MessageCacheImpl: MessageCache {
    private val messages = mutableListOf<MessageEntity>()

    private fun print(){
        println("Message $messages")
    }

    override suspend fun saveMessageId(chatId: String, message: Pair<Int, MessageGroup>): MessageEntity {
        val entity = MessageEntity(
            id = message.first,
            chatId = chatId,
            group = message.second
        )
        messages.add(
            entity
        )
        print()
        return entity
    }

    override suspend fun deleteMessageId(chatId: String, messageId: Int): MessageEntity {
        return messages.removeAt(messages.indexOfFirst { it.id == messageId && it.chatId == chatId })
    }


    override suspend fun getMessageIds(chatId: String, term: (MessageEntity) -> Boolean): List<MessageEntity> {
        return messages.filter{ term(it) && it.chatId == chatId }
    }
}