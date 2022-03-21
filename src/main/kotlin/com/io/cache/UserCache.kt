package com.io.cache

import com.io.cache.entity.UserEntity
import com.io.cache.entity.UserState

interface UserCache {

    suspend fun getUser(chatId: String): UserEntity?

    suspend fun saveUser(userId: String): Boolean

    suspend fun updateStateUser(chatId: String, state: UserState)
}