package com.io.cache

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.SectionRuleEntity
import com.io.model.TypeMessage
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT
import com.io.util.GetString


interface MessageCache {
    suspend fun saveMessageId(chatId: String, messageId: Int, messageGroup: MessageGroup): MessageEntity

    suspend fun deleteMessage(chatId: String, messageId: Int): MessageEntity?

    suspend fun getMessages(
        chatId: String,
        searchRule: GetBooleanViaT<MessageEntity>
    ): List<MessageEntity>
}