package com.io.service.impl

import com.io.service.UserService
import com.io.service.entity.MessageEntity
import com.io.service.entity.UserEntity
import com.io.service.entity.UserState
import com.io.model.Language
import com.io.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserServiceImpl(
   private val messageRepository: MessageRepository
): UserService {
    private val users = mutableListOf<UserEntity>()

    override suspend fun getUser(chatId: String): UserEntity? = withContext(Dispatchers.IO){
        return@withContext users.find { it.chatId == chatId }
    }

    override suspend fun saveMessageIds(chatId: String, messages: List<Int>) {
        messageRepository.saveMessageIds(chatId, messages)
    }

    override suspend fun getMessageIds(chatId: String, isDeleteMessage: () -> Boolean) {
        messageRepository.getMessageIds(chatId,isDeleteMessage)
    }

    override suspend fun deleteMessageIds(chatId: String, messages: List<Int>) {
        messageRepository.deleteMessageIds(chatId, messages)
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