package com.io.repository

interface MessageRepository {
    suspend fun saveMessageIds(chatId: String, messages: List<Int>)

    suspend fun getMessageIds(chatId: String, isDeleteMessage: () -> Boolean): List<Int>

    suspend fun deleteMessageIds(chatId: String, messages: List<Int>)
}