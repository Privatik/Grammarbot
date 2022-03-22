package com.io.repository.impl

import com.io.repository.MessageRepository
import com.io.service.entity.MessageEntity

class MessageRepositoryImpl: MessageRepository {
    private val messages = mutableListOf<MessageEntity>()

    override suspend fun saveMessageIds(chatId: String, messages: List<Int>) {
        messages.forEach {
            this.messages.add(
                MessageEntity(
                    id = it,
                    chatId = chatId
                )
            )
        }
    }

    override suspend fun getMessageIds(chatId: String, isDeleteMessage: () -> Boolean): List<Int> {
        return messages.filter{ isDeleteMessage() }.map { it.id }
    }

    override suspend fun deleteMessageIds(chatId: String, messages: List<Int>) {
        messages.forEach {
            this.messages.removeIf { it.chatId == chatId }
        }
    }

}