package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity

class MessageCacheImpl: MessageCache {
    private val messages = mutableListOf<MessageEntity>()

    private fun print(){
        println("Message $messages")
    }

    override suspend fun saveMessageIds(chatId: String, messages: List<Pair<String, Int>>): Boolean {
        messages.forEach {
            this.messages.add(
                MessageEntity(
                    id = it.second,
                    chatId = chatId,
                    group = it.first
                )
            )
        }
        print()
        return true
    }


    override suspend fun getMessageIds(chatId: String, term: (MessageEntity) -> Boolean): Map<String, List<Int>> {
        return messages.filter{ term(it) && it.chatId == chatId }.groupByTo(hashMapOf(), {it.group}, {it.id})

    }

    override suspend fun deleteMessageId(chatId: String, messages: List<Int>) {
        messages.forEach {
            this.messages.removeIf { it.chatId == chatId }
        }
        print()
    }
}