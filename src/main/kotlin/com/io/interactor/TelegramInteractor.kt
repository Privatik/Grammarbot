package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity

interface TelegramInteractor {

    fun saveMessage(chaId: String, messageIds: List<Int>): Boolean

    fun getMessage(chaId: String, term: MessageEntity.() -> Boolean ):Map<String,Int>
}

class TelegramInteractorImpl(
    private val messageCache: MessageCache
): TelegramInteractor {

    override fun saveMessage(chaId: String, messageIds: List<Int>): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMessage(chaId: String, term: MessageEntity.() -> Boolean): Map<String,Int> {
        TODO("Not yet implemented")
    }

}