package com.io.cache.impl

import com.io.cache.UserCache
import com.io.cache.entity.UserEntity
import com.io.cache.entity.UserState
import com.io.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserCacheImpl: UserCache {
    private val users = mutableListOf<UserEntity>()

    override suspend fun getUser(chatId: String): UserEntity? = withContext(Dispatchers.IO){
        return@withContext users.find { it.chatId == chatId }
    }

    override suspend fun saveUser(chatId: String): Boolean = withContext(Dispatchers.IO){
        if (users.find { it.chatId == chatId } != null) return@withContext false
        val user = UserEntity(
            chatId = chatId,
            currentLanguage = Language.EN,
            currentState = UserState.RELAX
        )
        users.add(user)
        return@withContext true
    }

    override suspend fun updateStateUser(chatId: String, state: UserState) = withContext(Dispatchers.IO){
        val index = users.indexOfFirst { it.chatId == chatId }
        users[index] = users[index].copy(currentState = state)
    }
}