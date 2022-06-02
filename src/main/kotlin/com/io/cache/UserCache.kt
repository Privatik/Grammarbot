package com.io.cache

import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.model.UserState

interface UserCache {

    suspend fun getUser(chatId: String): UserEntity?

    suspend fun saveUser(chatId: String): UserEntity

    suspend fun updateStateUser(chatId: String, language: Language? = null, state: UserState? = null): UserEntity
}