package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.SectionCache
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.SectionRuleEntity
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaMessageEntity
import com.io.util.GetString

class MessageCacheImpl(
    private val sectionCache: SectionCache
): MessageCache {
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

    override suspend fun getMessageIds(chatId: String, term: GetBooleanViaMessageEntity): List<MessageEntity> {
        return messages.filter{ term(it) && it.chatId == chatId }
    }

    override suspend fun getSettingRuleEntity(get: GetString): SectionRuleEntity {
        return sectionCache.getCurrentRules(get())
    }

}