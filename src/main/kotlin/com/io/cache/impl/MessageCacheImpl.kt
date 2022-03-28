package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup

class MessageCacheImpl: MessageCache {
    private val messages = mutableListOf<MessageEntity>()

    override suspend fun saveMessageIds(chatId: String, messages: List<Int>) {
        messages.forEach {
            this.messages.add(
                MessageEntity(
                    id = it,
                    chatId = chatId,
                    group = MessageGroup.START
                )
            )
        }
    }


    override suspend fun getMessageIds(chatId: String, term: (MessageEntity) -> Boolean): List<Int> {
        return messages.filter{ term(it) && it.chatId == chatId }.map { it.id }
    }

    override suspend fun deleteMessageIds(chatId: String, messages: List<Int>) {
        messages.forEach {
            this.messages.removeIf { it.chatId == chatId }
        }
    }
}