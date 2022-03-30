package com.io.cache.impl

import com.io.cache.MessageCache
import com.io.cache.UserCache
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.model.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserCacheImpl (
    private val messageCache: MessageCache
): UserCache {
    private val users = mutableListOf<UserEntity>()

    private fun print(){
        println("User $users")
    }

    override suspend fun getUser(chatId: String): UserEntity? = withContext(Dispatchers.IO){
        return@withContext users.find { it.chatId == chatId }
    }

    override suspend fun saveUser(chatId: String): UserEntity = withContext(Dispatchers.IO){
        if (users.find { it.chatId == chatId } != null) error("This user exits")
        val user = UserEntity(
            chatId = chatId,
            currentLanguage = Language.EN,
            currentState = UserState.INITIAL
        )
        users.add(user)
        print()
        return@withContext user
    }

    override suspend fun updateStateUser(
        chatId: String,
        language: Language?,
        state: UserState?
    ) = withContext(Dispatchers.IO){
        val index = users.indexOfFirst { item -> item.chatId == chatId }

        check(index != -1){
            "Not find this user"
        }

        val oldUser = users[index]
        val newUser = oldUser.copy(
            chatId = chatId,
            currentLanguage = language ?: oldUser.currentLanguage,
            currentState = state ?: oldUser.currentState
        )
        users[index] = newUser
        print()
        return@withContext newUser
    }
}