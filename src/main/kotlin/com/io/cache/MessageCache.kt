package com.io.cache

import com.io.cache.entity.MessageEntity

interface MessageCache {
    suspend fun saveMessageIds(chatId: String, messages: List<Pair<String, Int>>): Boolean

    suspend fun getMessageIds(chatId: String, term: (MessageEntity) -> Boolean): Map<String,List<Int>>

    suspend fun deleteMessageIds(chatId: String, messages: List<Int>)
}