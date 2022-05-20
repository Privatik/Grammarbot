package com.io.interactor

import com.io.cache.UserCache
import com.io.cache.entity.UserEntity
import com.io.model.Language
import com.io.model.UserState
import com.io.util.GetUserEntity

interface UserInteractor<T>{

    suspend fun getOrSaveNewUser(chaId: String): T

    suspend fun updateUser(chatId: String, language: Language? = null, state: UserState? = null): T

    sealed class BehaviorForUser {
        data class Update(
            val language: Language? = null,
            val state: UserState? = null
        ): BehaviorForUser()
        object GetOrCreate: BehaviorForUser()
        object None: BehaviorForUser()
    }
}

class UserInteractorImpl(
    private val userCache: UserCache
): UserInteractor<GetUserEntity>{

    override suspend fun getOrSaveNewUser(chaId: String): GetUserEntity {
        return {
            userCache.getUser(chaId) ?: userCache.saveUser(chaId)
        }
    }

    override suspend fun updateUser(chatId: String, language: Language?, state: UserState?): GetUserEntity {
        return {
            userCache.updateStateUser(chatId, language, state)
        }
    }


}