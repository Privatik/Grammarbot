package com.io.cache

import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup

interface MessageCache {
    suspend fun saveMessageIds(chatId: String, messages: List<Int>)

    suspend fun getMessageIds(chatId: String, term: (MessageEntity) -> Boolean): List<Int>

    suspend fun deleteMessageIds(chatId: String, messages: List<Int>)
}