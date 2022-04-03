package com.io.cache

import com.io.cache.entity.MessageEntity

interface MessageCache {
    suspend fun saveMessageId(chatId: String, messages: Pair<String, Int>): Boolean
    suspend fun deleteMessageId(chatId: String, messages: Int)

    suspend fun getMessageIds(chatId: String, term: (MessageEntity) -> Boolean): Map<String,List<Int>>
}