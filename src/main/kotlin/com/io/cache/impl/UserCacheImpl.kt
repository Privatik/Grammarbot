package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.UserCache
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.cache.entity.UserState
import com.io.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserCacheImpl (
    private val messageCache: MessageCache
): UserCache {
    private val users = mutableListOf<UserEntity>()

    override suspend fun getUser(chatId: String): UserEntity? = withContext(Dispatchers.IO){
        return@withContext users.find { it.chatId == chatId }
    }

    override suspend fun saveMessageIds(chatId: String, messages: List<Int>) {
        messageCache.saveMessageIds(chatId, messages)
    }

    override suspend fun getMessageIds(chatId: String, isDeleteMessage: (MessageEntity) -> Boolean) {
        messageCache.getMessageIds(chatId,isDeleteMessage)
    }

    override suspend fun deleteMessageIds(chatId: String, messages: List<Int>) {
        messageCache.deleteMessageIds(chatId, messages)
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