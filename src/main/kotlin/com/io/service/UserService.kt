package com.io.service

import com.io.service.entity.UserEntity
import com.io.service.entity.UserState

interface UserService {

    suspend fun getUser(chatId: String): UserEntity?

    suspend fun saveMessageIds(chatId: String, messages: List<Int>)

    suspend fun getMessageIds(chatId: String, isDeleteMessage: () -> Boolean)

    suspend fun deleteMessageIds(chatId: String, messages: List<Int>)

    suspend fun saveUser(userId: String): Boolean

    suspend fun updateStateUser(chatId: String, state: UserState)
}