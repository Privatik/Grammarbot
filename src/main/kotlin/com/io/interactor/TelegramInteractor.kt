package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.UserCache
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.model.UserState

interface TelegramInteractor {

    suspend fun getOrSaveNewUser(chaId: String): UserEntity

    suspend fun updateUser(chatId: String, language: Language? = null, state: UserState? = null): UserEntity

    suspend fun saveMessage(chaId: String, messageIds: Pair<String, Int>): Boolean

    suspend fun deleteMessage(chaId: String, messageIds: Pair<String, Int>): Boolean

    suspend fun getMessage(chaId: String, term: (MessageEntity) -> Boolean ): Map<String, List<Int>>
}

class TelegramInteractorImpl(
    private val messageCache: MessageCache,
    private val userCache: UserCache
): TelegramInteractor {

    override suspend fun getOrSaveNewUser(chaId: String): UserEntity {
        val user = userCache.getUser(chaId)
        if (user != null) return user
        return userCache.saveUser(chaId)
    }

    override suspend fun updateUser(chatId: String, language: Language?, state: UserState?): UserEntity {
        return userCache.updateStateUser(chatId, language, state)
    }

    override suspend fun saveMessage(chaId: String, messageIds: Pair<String, Int>): Boolean {
        return messageCache.saveMessageId(chaId, messageIds)
    }

    override suspend fun deleteMessage(chaId: String, messageIds: Pair<String, Int>): Boolean {
        return messageCache.deleteMessageId(chaId, )
    }

    override suspend fun getMessage(chaId: String, term: (MessageEntity) -> Boolean): Map<String, List<Int>> {
        return messageCache.getMessageIds(chaId, term)
    }

}