package com.io.cache

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.cache.entity.UserState

interface UserCache {

    suspend fun getUser(chatId: String): UserEntity?

    suspend fun saveMessageIds(chatId: String, messages: List<Int>)

    suspend fun getMessageIds(chatId: String, isDeleteMessage: (MessageEntity) -> Boolean)

    suspend fun deleteMessageIds(chatId: String, messages: List<Int>)

    suspend fun saveUser(chatId: String): Boolean

    suspend fun updateStateUser(chatId: String, state: UserState)
}